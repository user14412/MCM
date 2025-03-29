<template>
  <div class="user-profile-post-container">
    <div class="card shadow-sm">
      <div class="card-header bg-primary text-white">
        <h5 class="mb-0">编辑文章</h5>
      </div>

      <div class="card-body">
        <div v-if="loading" class="d-flex justify-content-center align-items-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>

        <div v-else>
          <div class="mb-4">
            <label for="post-title" class="form-label fw-bold">文章标题</label>
            <input type="text" class="form-control form-control-lg" id="post-title" v-model="article.title"
              placeholder="请输入文章标题" maxlength="200" />
            <div class="form-text text-end">{{ article.title.length }}/200</div>
          </div>

          <div class="mb-4">
            <label class="form-label fw-bold">文章内容</label>
            <QuillEditor ref="quillEditor" v-model:content="article.content" contentType="html" :options="editorOptions"
              style="height: 400px;" @ready="onEditorReady" />
          </div>

          <div class="d-flex justify-content-end gap-3 mt-4">
            <button class="btn btn-outline-secondary px-4" @click="handleCancel" :disabled="submitting">
              取消
            </button>

            <button class="btn btn-primary px-4" @click="handleSubmit" :disabled="submitting">
              <template v-if="submitting">
                <span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                提交中...
              </template>
              <template v-else>
                <i class="bi bi-check-circle me-2"></i>
                提交
              </template>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 模态框 -->
    <div class="modal fade" id="confirmModal" ref="confirmModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ modalTitle }}</h5>
            <button class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <p>{{ modalMessage }}</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
            <button class="btn btn-primary" @click="handleModalConfirm">确定</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { onMounted } from 'vue';
import { QuillEditor } from '@vueup/vue-quill'; // 引入编辑器组件
import '@vueup/vue-quill/dist/vue-quill.snow.css'; // 引入编辑器组件的样式
import { Modal } from 'bootstrap';
import axios from 'axios';
import { useStore } from 'vuex';
import router from '@/router';

export default {
  name: 'UserProfilePost',
  components: { QuillEditor },
  setup() {
    const store = useStore();

    // 响应式数据
    const article = ref({
      title: '', // v-model
      content: '', // v-model
    });

    const loading = ref(false);
    const submitting = ref(false);
    const quillEditor = ref(null); // 绑定组件的响应式对象，用来调用组件的相关操作的方法
    const confirmModal = ref(null);

    // 模态框相关状态
    const modalTitle = ref(''); // 标题
    const modalMessage = ref(''); //
    const modalOkText = ref('确定'); // 确认按钮文字
    const modalCallback = ref(null);

    // 编辑器配置
    const editorOptions = {
      theme: 'snow',
      modules: {
        toolbar: [
          ['bold', 'italic', 'underline', 'strike'],
          ['blockquote', 'code-block'],
          [{ header: 1 }, { header: 2 }],
          [{ list: 'ordered' }, { list: 'bullet' }],
          [{ script: 'sub' }, { script: 'super' }],
          [{ indent: '-1' }, { indent: '+1' }],
          [{ direction: 'rtl' }],
          [{ size: ['small', false, 'large', 'huge'] }],
          [{ header: [1, 2, 3, 4, 5, 6, false] }],
          [{ color: [] }, { background: [] }],
          [{ font: [] }],
          [{ align: [] }],
          ['clean'],
          ['link', 'image', 'video'],
        ],
      },
      placeholder: '请输入文章内容...',
    };

    // 显示模态框方法
    const showModal = (title, message, callback = null, okText = '确定') => {
      modalTitle.value = title;
      modalMessage.value = message;
      modalOkText.value = okText;
      modalCallback.value = callback;

      const modalInstance = new Modal(confirmModal.value); // 使用 new Modal
      modalInstance.show();
    };

    // 方法定义
    const loadArticle = async () => {
      try {
        loading.value = true;
        // 这里可以添加加载文章的逻辑
        // 例如从API获取数据
      } catch (error) {
        console.error('加载文章失败:', error);
        showModal('错误', '加载文章失败，请刷新重试');
      } finally {
        loading.value = false;
      }
    };

    const handleSubmit = async () => {
      // 返回一个promise对象
      if (!validateForm()) return;

      submitting.value = true;
      console.log('提交文章:', article.value);
      try {
        // 这里添加提交逻辑
        // DONE例如调用API保存文章
        // url data config
        axios
          .post(
            'http://127.0.0.1:3000/article/add/',
            // data
            {
              title: article.value.title,
              content: article.value.content,
            },
            // config
            {
              headers: {
                // 'Content-Type': 'application/json', // 明确声明JSON格式
                Authorization: 'Bearer ' + store.state.user.token,
              },
            }
          )
          // 成功的回调
          .then((resp) => {
            console.log('addArticle 请求成功:', resp);
          })
          // 失败的回调
          .catch((error) => {
            console.error('addArticle 请求失败:', error);
          });
        showModal('成功', '文章保存成功', () => {
          // DONE成功后的回调，例如跳转页面
          router.push({ name: 'user_profile_index' });
        });
      } catch (error) {
        console.error('提交失败:', error);
        showModal('错误', '提交失败，请稍后重试');
      } finally {
        submitting.value = false; // 恢复按钮可点击状态
      }
      console.log('调用handleSubmit');
    };

    // 点击取消按钮
    const handleCancel = () => {
      if (hasUnsavedChanges()) {
        showModal(
          '确认',
          '您有未保存的修改，确定要离开吗?',
          () => {
            // TODO确认离开的回调
            resetForm();
          },
          '确定离开'
        );
      } else {
        resetForm();
      }
      console.log('调用handleCancel');
    };

    const validateForm = () => {
      if (!article.value.title.trim()) {
        showModal('提示', '请输入文章标题');
        return false;
      }

      const textContent = quillEditor.value.getText().trim(); //去除文本首尾两端空白字符
      if (textContent.length < 10) {
        showModal('提示', '文章内容不能少于10个字符');
        return false;
      }

      return true;
    };

    const hasUnsavedChanges = () => {
      // 标题或内容为空
      return (
        article.value.title.trim() ||
        article.value.content.trim() ||
        quillEditor.value.getText().trim()
      );
    };

    const resetForm = () => {
      article.value.title = '';
      article.value.content = '';
      // TODO这里可以添加重置后的逻辑，例如跳转页面
    };

    // 点击确认之后的回调
    const handleModalConfirm = () => {
      if (modalCallback.value) modalCallback.value();
      const modalInstance = Modal.getInstance(confirmModal.value); // 获取Modal实例
      modalInstance.hide();
    };

    const onEditorReady = (quill) => {
      // 编辑器准备就绪后的回调
      console.log('编辑器已准备就绪', quill);
    };

    // 生命周期钩子
    onMounted(() => {
      confirmModal.value = document.getElementById('confirmModal');
      loadArticle();
    });

    return {
      article,
      loading,
      editorOptions,
      onEditorReady,
      submitting,
      handleCancel,
      quillEditor,
      handleSubmit,
      confirmModal,
      modalTitle,
      modalMessage,
      modalOkText,
      handleModalConfirm,
    };
  },
};
</script>

<style scoped>
.modal-dialog {
  margin-top: 8vh !important;
  /* 下移8%视口高度 */
  transform: translateY(0) !important;
  /* 禁用默认居中转换 */
}

.user-profile-post-container {
  max-width: 900px;
  margin: 0 auto;
}

/* 自定义编辑器样式 */
:deep(.ql-toolbar) {
  border-top-left-radius: 0.375rem !important;
  border-top-right-radius: 0.375rem !important;
  border-color: #dee2e6 !important;
}

:deep(.ql-container) {
  border-bottom-left-radius: 0.375rem !important;
  border-bottom-right-radius: 0.375rem !important;
  border-color: #dee2e6 !important;
  font-family: inherit;
}

:deep(.ql-editor) {
  min-height: 300px;
  font-size: 1rem;
  line-height: 1.6;
}
</style>