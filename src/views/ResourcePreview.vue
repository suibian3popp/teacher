<template>
  <div class="resource-preview-container">
    <div class="header">

      <el-breadcrumb separator="/">
<!--        <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>-->
        <el-breadcrumb-item :to="{ path: '/resource/list/'+ownerId }">资源列表</el-breadcrumb-item>
        <el-breadcrumb-item>资源预览</el-breadcrumb-item>
      </el-breadcrumb>
      <h3 class="page-title">{{ fileName }}</h3>
    </div>

    <el-card v-loading="loading" class="preview-card">
      <!-- 图片预览 -->
      <div v-if="isImage" class="preview-image-container">
        <img :src="previewUrl" alt="预览图" class="preview-image">
      </div>

      <!-- PDF预览 - 使用PDF.js -->
      <div v-else-if="isPdf" class="preview-pdf-container">
        <div class="pdf-viewer">
          <!-- PDF渲染区域 -->
          <div class="pdf-navigation" v-if="pdfPages > 1">
            <el-button size="small" @click="prevPage" :disabled="currentPage === 1">上一页</el-button>
            <span class="page-info">第 {{ currentPage }} / {{ pdfPages }} 页</span>
            <el-button size="small" @click="nextPage" :disabled="currentPage === pdfPages">下一页</el-button>
          </div>
          <div class="pdf-canvas-container">
            <canvas id="pdf-canvas"></canvas>
          </div>
          <div class="pdf-error" v-if="pdfError">
            <el-alert title="PDF预览失败" type="error" :description="pdfError" show-icon></el-alert>
          </div>
        </div>
      </div>

      <!-- 文本预览 -->
      <div v-else-if="isText" class="preview-text-container">
<!--        <el-tabs v-model="activeTab" class="text-tabs">-->
<!--          <el-tab-pane label="预览">-->
<!--            <pre class="text-preview" v-html="textContent"></pre>-->
<!--          </el-tab-pane>-->
<!--          <el-tab-pane label="下载">-->
<!--            <a :href="previewUrl" download class="download-link">点击下载</a>-->
<!--          </el-tab-pane>-->
<!--        </el-tabs>-->
        <div v-if="textContent" class="text-preview-wrapper">
          <pre class="text-preview" v-html="textContent"></pre>
        </div>
        <div v-else class="empty-text-state">
          <el-skeleton animated />
        </div>
      </div>

      <!-- 不支持的类型 -->
      <div v-else class="unsupported-container">
        <el-empty description="不支持的文件类型，点击下方按钮下载"></el-empty>
        <el-button type="primary" @click="handleDownload(resourceId)">下载文件</el-button>
      </div>
    </el-card>

    <div class="action-buttons">
      <el-button type="primary" @click="handleDownload(resourceId)">下载</el-button>
      <el-button @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, watch, onUnmounted, toRaw, nextTick} from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { doGet } from "@/http/httpRequest"
import axios from "axios";
import * as pdfjsLib from 'pdfjs-dist'
import 'pdfjs-dist/build/pdf.worker.min.mjs'

const route = useRoute()
const router = useRouter()

const resourceId = ref(0)
//路由参数变化时校验
watch(() => route.query.id, (val) => {
  const numVal = Number(val)
  resourceId.value = isNaN(numVal) ? 0 : numVal
}, { immediate: true })

const ownerId = ref(route.query.ownerId || 1000)

const loading = ref(true)
const previewUrl = ref('')
const fileName = ref('')
const fileType = ref('')
// const activeTab = ref('preview')
const textContent = ref('')

//判断文件类型
const isImage = ref(false)
const isPdf = ref(false)
const isText = ref(false)

//PDF.js相关状态
const pdfDoc = ref(null)
const currentPage = ref(1)
const pdfPages = ref(0)
const pdfError = ref('')
let canvas = null
let context = null
let pdfRenderingTask = null
const textLoading = ref(false)

//生命周期钩子 组件挂载时获取预览信息
onMounted(() => {
  fetchPreviewInfo()
})

//监听路由变化
watch(() => route.query, () => {
  resourceId.value = route.query.id || 0
  fetchPreviewInfo()
})

//组件卸载时清理资源
onUnmounted(() => {
  if (pdfRenderingTask) {
    pdfRenderingTask.cancel()
  }
})

//获取预览信息
const fetchPreviewInfo = async () => {
  loading.value = true
  textLoading.value = false

  doGet(`/api/resource/preview/${resourceId.value}`)
      .then(async (response) => {
        console.log('接口完整响应:', response);
        if (response.data.code === 200) {
          previewUrl.value = response.data.data.previewUrl
          console.log("previewUrl",previewUrl.value)
          fileName.value = response.data.data.fileName
          fileType.value = response.data.data.fileType.toLowerCase()

          //判断文件类型
          isImage.value = /.jpg|.jpeg|.png|.gif|.bmp|.webp/.test(fileType.value);
          isPdf.value = fileType.value === '.pdf'
          isText.value = /.txt|.md|.json|.xml|.html|.css|.js/.test(fileType.value)

          //如果是PDF类型，初始化PDF.js
          if (isPdf.value) {
            await renderPdf()
          }

          //如果是文本类型，获取文本内容
          if (isText.value) {
            try {
              textLoading.value=true;
              const textResponse = await fetch(previewUrl.value)
              if (textResponse.ok) {
                textContent.value = await textResponse.text()
                //对代码进行语法高亮处理
                textContent.value = textContent.value
                    .replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/\n/g, '<br>')

                await nextTick()
              }
            } catch (error) {
              console.error('获取文本内容失败', error)
              ElMessage.error('获取文本内容失败')
              loading.value = false
            } finally{
              textLoading.value = false
            }
          }
        } else {
          ElMessage.error(response.data.msg || '获取预览信息失败')
          // await router.push('/resource/list/' + ownerId.value)
          loading.value = false
        }
        loading.value = false
      })
      .catch((error) => {
        console.error('请求异常', error)
        ElMessage.error('网络异常，请重试')
        // await router.push('/resource/list/' + ownerId.value)
        loading.value = false
        textLoading.value = false
      })

}

//渲染PDF文件
const renderPdf = async () => {
  if (!previewUrl.value) return

  pdfError.value = ''

  try {
    //设置PDF.js worker路径
    pdfjsLib.GlobalWorkerOptions.workerSrc = new URL(
        'pdfjs-dist/build/pdf.worker.min.mjs',
        import.meta.url
    ).toString();

    //加载PDF文件
    const loadingTask = pdfjsLib.getDocument({
      url: previewUrl.value,
    })

    //获取PDF文档对象
    pdfDoc.value = await loadingTask.promise
    pdfPages.value = pdfDoc.value.numPages

    //渲染第一页
    renderPage(1)
  } catch (error) {
    console.error('PDF加载失败:', error)
    pdfError.value = `PDF预览失败: ${error.message}`
  }
}

//渲染指定页
const renderPage = (num) => {
  if (!pdfDoc.value) return

  currentPage.value = num

  //取消之前的渲染任务
  if (pdfRenderingTask) {
    pdfRenderingTask.cancel()
  }

  //获取canvas元素
  if (!canvas) {
    canvas = document.getElementById('pdf-canvas')
    context = canvas.getContext('2d')
  }

//使用toRaw获取原始对象，避免Proxy代理问题
  const rawPdfDoc = toRaw(pdfDoc.value)

  //获取指定页
  rawPdfDoc.getPage(num).then((page) => {
    //设置缩放比例
    const viewport = page.getViewport({ scale: 1.5 })

    //设置canvas尺寸
    canvas.height = viewport.height
    canvas.width = viewport.width

    //渲染PDF页
    const renderContext = {
      canvasContext: context,
      viewport: viewport
    }

    pdfRenderingTask = page.render(renderContext)
    pdfRenderingTask.promise.then(() => {
      console.log(`第 ${num} 页渲染完成`)
    }).catch((error) => {
      if (error.name !== 'RenderingCancelled') {
        console.error('PDF渲染错误:', error)
        pdfError.value = `PDF渲染失败: ${error.message}`
      }
    })
  })
}

//上一页
const prevPage = () => {
  if (currentPage.value <= 1) return
  renderPage(currentPage.value - 1)
}

//下一页
const nextPage = () => {
  if (currentPage.value >= pdfPages.value) return
  renderPage(currentPage.value + 1)
}

//下载
function handleDownload(resourceId){
  let iframe = document.createElement("iframe");
  iframe.src = axios.defaults.baseURL + "/api/resource/download/" +resourceId;
  iframe.style.display = "none";
  document.body.appendChild(iframe);
}

//返回列表
const goBack = () => {
  router.push('/resource/list/' + ownerId.value)
}
</script>

<style scoped>
.resource-preview-container {
  padding: 20px;
}
.header {
  margin-bottom: 20px;
}
.page-title {
  font-size: 20px;
  font-weight: 600;
  margin: 10px 0;
}
.preview-card {
  margin-bottom: 20px;
}
.preview-image-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  background-color: #f5f7fa;
}
.preview-image {
  max-width: 100%;
  max-height: 600px;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
.preview-pdf-container {
  width: 100%;
  min-height: 600px;
  background-color: #f5f7fa;
}
.pdf-viewer {
  padding: 20px;
}
.pdf-navigation {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 10px;
}
.page-info {
  line-height: 32px;
}
.pdf-canvas-container {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: white;
  overflow: auto;
  display: flex;
  justify-content: center;
  padding: 10px;
  min-height: 500px;
}
.pdf-error {
  margin-top: 10px;
}
.preview-text-container {
  width: 100%;
}
.text-tabs {
  width: 100%;
}
.text-preview {
  white-space: pre-wrap;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 4px;
  overflow-x: auto;
  font-family: monospace;
  max-height: 600px;
  overflow-y: auto;
}
.unsupported-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
}
.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
.download-link {
  display: inline-block;
  padding: 8px 16px;
  background-color: #409EFF;
  color: white;
  border-radius: 4px;
  text-decoration: none;
}
.download-link:hover {
  background-color: #66b1ff;
}

.text-preview-wrapper {
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 4px;
  overflow: auto;
  font-family: monospace;
  max-height: 600px;
}

.empty-text-state {
  padding: 20px;
}
</style>