<template>
  <div class="user-profile-write">
    <div class="card shadow-lg">
      <div class="card-body p-4">
        <!-- 文章标题 -->
        <div class="row mb-4">
          <label class="col-md-2 col-form-label fw-bold">标题</label>
          <div class="col-md-10">
            <input 
              v-model="article.title"
              type="text" 
              class="form-control form-control-lg"
              placeholder="请输入文章标题"
              :class="{ 'is-invalid': errors.title }"
            >
            <div v-if="errors.title" class="invalid-feedback">
              {{ errors.title }}
            </div>
          </div>
        </div>

        <!-- 富文本编辑器 -->
        <div class="row mb-4">
          <label class="col-md-2 col-form-label fw-bold">内容</label>
          <div class="col-md-10">
            <Editor
              v-model="article.content"
              api-key="your-tinymce-key"
              :init="editorConfig"
              :class="{ 'editor-error': errors.content }"
            />
            <div v-if="errors.content" class="text-danger small mt-2">
              {{ errors.content }}
            </div>
          </div>
        </div>

        <!-- 分类选择 -->
        <div class="row mb-4">
          <label class="col-md-2 col-form-label fw-bold">分类</label>
          <div class="col-md-4">
            <select 
              v-model="article.category"
              class="form-select"
            >
              <option 
                v-for="category in categories" 
                :key="category.id"
                :value="category.id"
              >
                {{ category.name }}
              </option>
            </select>
          </div>
        </div>

        <!-- 封面图片上传 -->
        <div class="row mb-4">
          <label class="col-md-2 col-form-label fw-bold">封面图</label>
          <div class="col-md-10">
            <div class="border-dashed p-3 rounded-3">
              <input 
                type="file"
                accept="image/*"
                class="form-control"
                @change="handleCoverUpload"
              >
              <div v-if="article.cover" class="mt-3">
                <img 
                  :src="article.cover" 
                  class="preview-img img-thumbnail"
                  alt="封面预览"
                >
              </div>
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="d-flex justify-content-end gap-3 mt-5">
          <button 
            class="btn btn-primary px-5"
            @click="submitArticle"
          >
            发布
          </button>
          <button 
            class="btn btn-outline-secondary px-5"
            @click="resetForm"
          >
            重置
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import Editor from '@tinymce/tinymce-vue'
import axios from 'axios'

export default {
  components: { Editor },
  props: {
    categories: {
      type: Array,
      default: () => [
        { id: 1, name: '技术分享' },
        { id: 2, name: '生活随笔' }
      ]
    }
  },
  setup(props, { emit }) {
    const article = ref({
      title: '',
      content: '',
      category: props.categories[0]?.id || 1,
      cover: null
    })

    const errors = ref({})

    const uploadImage = async (file) => {
      try {
        const formData = new FormData()
        formData.append('image', file)
        const response = await axios.post('/api/upload', formData)
        return response.data.url
      } catch (error) {
        console.error('图片上传失败:', error)
        return ''
      }
    }
    
    // TinyMCE配置
    const editorConfig = {
      height: 500,
      menubar: 'file edit view format tools',
      plugins: 'code table lists link image',
      toolbar: 'undo redo | formatselect | bold italic | alignleft aligncenter alignright | bullist numlist outdent indent | code',
      images_upload_handler: async (blobInfo) => {
        const file = blobInfo.blob()
        return await uploadImage(file)
      }
    }

    // 处理封面图片上传
    const handleCoverUpload = (e) => {
      const file = e.target.files[0]
      if (file) {
        const reader = new FileReader()
        reader.onload = () => {
          article.value.cover = reader.result
        }
        reader.readAsDataURL(file)
      }
    }

    // 表单验证
    const validateForm = () => {
      errors.value = {}
      let isValid = true

      if (!article.value.title.trim()) {
        errors.value.title = '标题不能为空'
        isValid = false
      }

      if (!article.value.content.trim()) {
        errors.value.content = '内容不能为空'
        isValid = false
      }

      return isValid
    }

    // 提交文章
    const submitArticle = async () => {
      if (!validateForm()) return

      try {
        const response = await axios.post('/api/articles', {
          ...article.value,
          cover: article.value.cover // 实际开发需要处理为文件上传
        })
        
        emit('submitted', response.data)
        resetForm()
      } catch (error) {
        console.error('提交失败:', error)
      }
    }

    // 重置表单
    const resetForm = () => {
      article.value = {
        title: '',
        content: '',
        category: props.categories[0]?.id || 1,
        cover: null
      }
      errors.value = {}
    }

    return {
      article,
      errors,
      editorConfig,
      handleCoverUpload,
      submitArticle,
      resetForm,
      uploadImage,
    }
  }
}
</script>

<style scoped>
.user-profile-write {
  max-width: 1200px;
  margin: 2rem auto;
}

.border-dashed {
  border: 2px dashed #dee2e6;
  transition: border-color 0.3s ease;
}

.border-dashed:hover {
  border-color: #0d6efd;
}

.preview-img {
  max-width: 200px;
  max-height: 150px;
  object-fit: cover;
}

.editor-error {
  border: 1px solid #dc3545;
  border-radius: 4px;
}
</style>