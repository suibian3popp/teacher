<template>
  <div class="resource-list-container">
    <h3 class="page-title" style="margin:auto; text-align: center">资源列表</h3>

    <!-- 筛选区域 -->
    <el-form
        ref="filterFormRef"
        :model="filterForm"
        label-width="100px"
        class="filter-form"
    >
      <!-- 资源难度筛选 -->
      <el-form-item label="资源难度">
        <el-select
            v-model="filterForm.difficulty"
            placeholder="请选择难度"
            style="width: 200px"
        >
          <el-option label="全部" value="" />
          <el-option label="初级" value="beginner" />
          <el-option label="中级" value="intermediate" />
          <el-option label="高级" value="advanced" />
        </el-select>
      </el-form-item>

      <!-- 资源类型筛选 -->
      <el-form-item label="资源类型">
        <el-select
            v-model="filterForm.type"
            placeholder="请选择类型"
            style="width: 200px"
        >
          <el-option label="全部" value="" />
          <el-option label="课件" value="courseware" />
          <el-option label="案例" value="case" />
          <el-option label="作业" value="assignment" />
          <el-option label="其他" value="other" />
        </el-select>
      </el-form-item>

      <!-- 访问权限筛选 -->
      <el-form-item label="访问权限">
        <el-select
            v-model="filterForm.permission"
            placeholder="请选择权限"
            style="width: 200px"
        >
          <el-option label="全部" value="" />
          <el-option label="私人" value="private" />
          <el-option label="院系可见" value="department" />
          <el-option label="公开" value="public" />
        </el-select>
      </el-form-item>

      <!-- 排序方式 -->
      <el-form-item label="排序方式">
        <el-select
            v-model="filterForm.sortBy"
            placeholder="请选择排序依据"
            style="width: 200px"
        >
          <el-option label="创建时间" value="uploadTime" />
          <el-option label="资源名称" value="name" />
          <el-option label="文件大小" value="fileSize" />
        </el-select>
      </el-form-item>

      <!-- 排序方向 -->
      <el-form-item label="排序方向">
        <el-radio-group v-model="filterForm.sortDir">
          <el-radio value="asc">升序</el-radio>
          <el-radio value="desc">降序</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 操作按钮 -->
      <el-form-item label="&nbsp">
        <el-button type="primary" @click="fetchResources">查询</el-button>
        <el-button type="success" plain @click="resetFilter">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton animated />
    </div>

    <!-- 空状态 -->
    <div v-else-if="!loading && resourceList.length === 0" class="empty-container">
      <el-empty description="暂无资源" />
    </div>
    
    <!-- 资源列表展示 -->
    <el-table
        v-else
        :data="formattedResourceList"
        border
        style="width: 100%; margin-top: 20px">

      <el-table-column
          label="资源名称"
          min-width="180">
        <!-- 自定义渲染资源名称 -->
        <template #default="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>

      <el-table-column
          label="资源类型"
          width="120">
        <template #default="scope">
          {{ typeMap[scope.row.type] || scope.row.type }}
        </template>
      </el-table-column>

      <el-table-column
          label="资源难度"
          width="120">
        <template #default="scope">
          {{ difficultyMap[scope.row.difficulty] || scope.row.difficulty }}
        </template>
      </el-table-column>

      <el-table-column
          label="访问权限"
          width="120">
        <template #default="scope">
          {{ permissionMap[scope.row.permission] || scope.row.permission }}
        </template>
      </el-table-column>

      <el-table-column
          prop="uploadTime"
          label="上传时间"
          width="180"
      />

      <el-table-column
          label="操作"
          width="120">
        <template #default="scope">
          <el-button
              type="primary"
              size="small"
              @click="handleDownload(scope.row.resourceId)">
            下载
          </el-button>
          <el-button
              type="info"
              size="small"
              @click="handleView(scope.row.resourceId)">
            查看
          </el-button>
          <el-button
              type="info"
              size="small"
              @click="handleDelete(scope.row.resourceId)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
        v-if="!loading && pagination.total > 0"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 20px; text-align: right"
    />
  </div>
</template>

<script setup>
import {ref, reactive, onMounted, computed} from 'vue'
import { ElMessage} from 'element-plus'
import { useRouter } from 'vue-router'
import {doDelete, doGet} from "@/http/httpRequest";
import axios from "axios";
import qs from 'qs';
import dayjs from "dayjs";

//调用路由方法
const router = useRouter()
//获取动态路由中的id参数
// let route=useRoute()
// let ownerId=route.params.ownerId
const ownerId = ref(1000)

//筛选表单数据
const filterForm = reactive({
  difficulty: '', //资源难度
  type: '', //资源类型
  permission: '', //访问权限
  sortBy: 'uploadTime', //排序依据，默认按上传时间
  sortDir: 'desc' //排序方向，默认降序
})

//分页相关数据
const loading = ref(false)//加载状态
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

//表单引用（用于校验）
const filterFormRef = ref(null)

//初始化获取资源列表
onMounted(() => {
  fetchResources()
})

//枚举映射关系
const typeMap = {
  courseware: '课件',
  case: '案例',
  assignment: '作业',
  other: '其他'
}
const difficultyMap = {
  beginner: '初级',
  intermediate: '中级',
  advanced: '高级'
}
const permissionMap = {
  private: '私人',
  department: '院系可见',
  public: '公开'
}

//原始资源列表
const resourceList = ref([])

//格式化函数
const formatDateTime = (dateTime) => {
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm:ss')
}

// 格式化后的列表（响应式，自动更新）
const formattedResourceList = computed(() => {
  return  resourceList.value.map(item => ({
    ...item,
    resourceId: item.resourceId,
    type: typeMap[item.type] || item.type,
    difficulty: difficultyMap[item.difficulty] || item.difficulty,
    permission: permissionMap[item.permission] || item.permission,
    uploadTime: formatDateTime(item.uploadTime)
  }))
})

//获取资源列表数据
//根据用户id获取查询
const fetchResources = async () => {
  loading.value = true

  //构造请求参数
  const params = {
    ...filterForm,
    page: pagination.currentPage,
    pageSize: pagination.pageSize
  }

  const serializedParams = qs.stringify(params, { arrayFormat: 'brackets' });

  doGet(`/api/resource/list/${ownerId.value}?${serializedParams}`)
      .then((res) => {
        if (res.data.code === 200) {
          resourceList.value = res.data.data.records
          pagination.total = res.data.data.total
          console.log("资源列表资源：",resourceList.value)
        } else {
          ElMessage.error('获取资源列表失败：' + res.data.msg)
          resourceList.value = [] //清空原始列表
          pagination.total = 0
        }
        loading .value = false
      })
      .catch((err) => {
        console.error('请求异常：', err)
        ElMessage.error('网络异常，请重试！')
        resourceList.value = []
        pagination.total = 0
        loading.value = false
      })

}

//处理每条页数改变
const handleSizeChange = (val) => {
  pagination.pageSize = val
  pagination.currentPage = 1
  fetchResources()
}

//当前页改变
const handleCurrentChange = (val) => {
  pagination.currentPage = val
  fetchResources()
}

//重置筛选条件
const resetFilter = () => {
  filterForm.difficulty = ''
  filterForm.type = ''
  filterForm.permission = ''
  filterForm.sortBy = 'uploadTime'
  filterForm.sortDir = 'desc'
  pagination.currentPage = 1
  pagination.pageSize = 10
  fetchResources()
}

//资源下载 使用iframe
function handleDownload(resourceId){
  let iframe = document.createElement("iframe");
  iframe.src = axios.defaults.baseURL + "/api/resource/download/" +resourceId;
  iframe.style.display = "none";
  document.body.appendChild(iframe);}

// const handleDownload = (row) => {
//   const downloadUrl = `/api/resources/download/${row.id}`
//   const a = document.createElement('a')
//   a.href = downloadUrl
//   a.download = row.name
//   a.style.display = 'none'
//   document.body.appendChild(a)
//   a.click()
//   document.body.removeChild(a)
// }

//资源查看
const handleView = (row) => {
  router.push({
    path: '/resource/view',
    query: {
      id: row.id,
      type: row.type
    }
  })
}

//删除
function handleDelete(id) {
  doDelete("/api/resource/" + id).then((resp) => {
    if (resp.data.code === 200) {
      //删除成功
      ElMessage({
        showClose: true,
        message: '删除成功',
        type: 'success',
      })
      window.location.reload();
    } else {
      //删除失败
      ElMessage({
        showClose: true,
        message: '删除失败',
        type: 'error',
      })
    }
  })
}

</script>

<style scoped>
.resource-list-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
.page-title {
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: 600;
}
.filter-form {
  margin-bottom: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #eee;
}
</style>