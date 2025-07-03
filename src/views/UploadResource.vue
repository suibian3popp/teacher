<!--资源上传页面,没有加其他板块内容 后续整合-->
<!--目前用户id写死为1000，后续根据登陆传入-->

<template>
  <div class="upload-container">
    <h3 class="page-title" style="margin:auto; text-align: center">上传教学资源</h3>

    <!-- 表单区域 -->
    <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px">

<!--      &lt;!&ndash; 资源名称 &ndash;&gt;-->
<!--      <el-form-item label="资源名称" prop="name">-->
<!--        <el-input-->
<!--            v-model="form.name"-->
<!--            placeholder="请输入资源名称"-->
<!--            @blur="checkResourceName"-->
<!--        />-->
<!--        <div v-if="nameExists" class="error-message">资源名称已存在，请选择其他名称</div>-->
<!--      </el-form-item>-->

      <!-- 资源类型 -->
      <el-form-item label="资源类型" prop="type">
        <el-select
            v-model="form.type"
            placeholder="请选择资源类型"
            style="width: 200px"
        >
          <el-option label="课件" value="courseware" />
          <el-option label="案例" value="case" />
          <el-option label="作业" value="assignment" />
          <el-option label="其他" value="other" />
        </el-select>
      </el-form-item>

      <!-- 资源难度 -->
      <el-form-item label="资源难度" prop="difficulty">
        <el-select
            v-model="form.difficulty"
            placeholder="请选择难度"
            style="width: 200px"
        >
          <el-option label="初级" value="beginner" />
          <el-option label="中级" value="intermediate" />
          <el-option label="高级" value="advanced" />
        </el-select>
      </el-form-item>

      <!-- 访问权限 -->
      <el-form-item label="访问权限" prop="permission">
        <el-radio-group v-model="form.permission">
          <el-radio value="private">私人</el-radio>
          <el-radio value="department">院系可见</el-radio>
          <el-radio value="public">公开</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 文件上传 -->
      <el-form-item label="上传文件">
        <el-upload
            ref="uploadImageRef"
            class="avatar-uploader"
            :limit="1"
            :on-success="resSuccess"
            :on-error="resError"
            :file-list="resFileList"
            :on-change="handleFileChange"
            :action="`http://localhost:8090/api/resource/upload`"
            :auto-upload="false"
            :show-file-list="true">
          <img v-if="imageUrl" :src="imageUrl" class="avatar"  alt="上传的文件预览"/>
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          <div class="el-upload__tip">支持常见文件格式上传</div>
        </el-upload>
        <!--      插值表达式-->
        <div>{{resTip}}</div>
      </el-form-item>

      <!-- 操作按钮 -->
      <el-form-item label="&nbsp">
<!--        <el-button type="primary" @click="submitUpload" :disabled="nameExists">提交</el-button>-->
        <el-button type="primary" @click="submitUpload" >提交</el-button>
        <el-button type="success" plain @click="goBack">返回</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>

    </el-form>

<!--    跳转到资源列表（暂定放这）-->
    <el-button
        type="primary"
        class="goto-resourcesList"
        @click="gotoResourcesList"
    >
      跳转到资源列表
    </el-button>
  </div>
</template>

<script setup>

//获取动态路由中的id参数(后续使用 目前写死)
// const route = useRoute()
//let ownerId=route.params.ownerId
import {doPost} from "@/http/httpRequest";

const router = useRouter()//调用路由方法
const ownerId = ref(1000) //实际项目中应从登录状态获取

import {ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import {useRouter} from "vue-router";
import {Plus} from "@element-plus/icons-vue";

let resTip=ref("")
// const nameExists = ref("")

//响应式表单数据
const form = reactive({
  // name: '',
  type: '',
  difficulty: 'intermediate', //默认中级
  permission: 'private',       //默认私人
  file: null                   //存储文件对象
})

//表单校验规则
const rules = {
  // name: [
  //   {
  //     required: true,
  //     message: '请输入资源名称',
  //     trigger: 'blur'
  //   }
  // ],
  type: [
    {
      required: true,
      message: '请选择资源类型',
      trigger: 'change'
    }
  ],
  permission: [
    {
      required: true,
      message: '请选择访问权限',
      trigger: 'change'
    }
  ]
}

//文件列表（用于el-upload组件展示）
const resFileList = ref([])
const imageUrl = ref('')
//表单引用（用于校验）
const formRef = ref(null)

//文件变化处理
const handleFileChange = (file, currentResFileList) => {
  form.file = file.raw //存储原始文件对象
  resFileList.value = currentResFileList //更新文件列表展示

  //如果是图片文件，显示预览图
  if(file.raw.type.indexOf('image/')!==-1){
    imageUrl.value = URL.createObjectURL(file.raw)
  }else{
    imageUrl.value = ''
  }

}

// //检查资源名称是否已经存在
// const checkResourceName = () => {
//   if(!form.name) return
//
//   axios.get(`/api/resource/checkName?ownerId=${ownerId.value}&name=${encodeURIComponent(form.name)}`)
//     .then(res => {
//       nameExists.value=res.data.data
//     })
//       .catch(err => {
//         console.log('检查名称失败',err)
//         ElMessage.error('名称检查失败，请稍后重试')
//       })
// }

//提交按钮
function submitUpload () {
  //表单校验
  formRef.value.validate((valid) => {
    if (!valid) {
      ElMessage.error('表单校验未通过，请检查！')
      return
    }
    // if (nameExists.value) {
    //   ElMessage.error('资源名称已存在，请选择其他名称')
    //   return
    // }
    if (!form.file) {
      ElMessage.error('请先选择文件！')
      return
    }

    //构造 FormData
    const formData = new FormData()
    formData.append('ownerId', ownerId.value)
    formData.append('file', form.file)
    // formData.append('name', form.name)
    formData.append('type', form.type)
    formData.append('difficulty', form.difficulty)
    formData.append('permission', form.permission)

    //调用上传接口
    doPost('/api/resource/upload', formData)
        .then((res) => {
          if (res.data.code === 200) { //后端返回code=200为成功
            ElMessage.success('资源上传成功！')
            resetForm() //上传成功后重置表单
          } else {
            ElMessage.error('上传失败：' + res.data.msg)
          }
        })
        .catch((err) => {
          console.error('上传请求异常：', err)
          ElMessage.error('网络异常，请重试！')
        })
  })
}

function resSuccess() {
  resFileList.value = []
  resTip = "资源上传成功"
}
function resError(){
  resFileList.value=[]
  resTip = "资源上传失败"
}

//重置表单
const resetForm = () => {
  // form.name = ''
  form.type = ''
  form.difficulty = 'intermediate'
  form.permission = 'private'
  form.file = null
  resFileList.value = []
  imageUrl.value = ''
  // nameExists.value = false
  formRef.value.resetFields() //重置表单校验状态
}

function goBack(){
  router.go (-1)
}

function gotoResourcesList() {
  router.push("/resourcesList");
}

</script>

<style scoped>
.upload-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  position: relative;
  min-height: 60vh;
}
.page-title {
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: 600;
}
.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.goto-resourcesList {
  position: absolute;
  bottom: 20px;
  right: 20px;
}
</style>