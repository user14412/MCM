<template>
  <div class="survey-management">
    <div class="header">
      <h2>问卷管理</h2>
      <el-button type="primary" @click="handleAdd">创建问卷</el-button>
    </div>

    <el-table
      :data="paginatedSurveys"
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="createtime" label="创建时间" width="180" />
      <el-table-column label="操作" width="300">
        <template #default="{ row }">
          <el-button @click="handleEdit(row)">编辑</el-button>
          <el-button @click="handleViewAnswers(row)">查看回答</el-button>
          <el-button type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalSurveys"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 创建/编辑问卷对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="70%"
    >
      <el-form :model="surveyForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="surveyForm.title" />
        </el-form-item>
        <el-form-item label="内容">
          <div class="question-editor">
            <div v-for="(question, index) in surveyForm.content" :key="index" class="question-item">
              <div class="question-header">
                <span>第 {{ index + 1 }} 题</span>
                <el-button type="danger" size="small" @click="removeQuestion(index)">删除</el-button>
              </div>
              <el-form-item label="类型">
                <el-select v-model="question.type">
                  <el-option label="单选题" value="single" />
                  <el-option label="多选题" value="multiple" />
                  <el-option label="简答题" value="text" />
                </el-select>
              </el-form-item>
              <el-form-item label="题干">
                <el-input v-model="question.question_stem" />
              </el-form-item>
              <el-form-item label="必做">
                <el-switch v-model="question.required" />
              </el-form-item>
              
              <!-- 单选题和多选题选项 -->
              <template v-if="question.type !== 'text'">
                <el-form-item label="选项">
                  <div v-for="(option, optIndex) in question.choice" :key="optIndex" class="option-item">
                    <el-input v-model="question.choice[optIndex]" />
                    <el-button type="danger" size="small" @click="removeOption(index, optIndex)">删除</el-button>
                  </div>
                  <el-button type="primary" size="small" @click="addOption(index)">添加选项</el-button>
                </el-form-item>
                <el-form-item label="自定义选项">
                  <el-switch v-model="question.add_choice" />
                </el-form-item>
                
                <!-- 多选题额外选项 -->
                <template v-if="question.type === 'multiple'">
                  <el-form-item label="最少选择">
                    <el-input-number v-model="question.min_choice" :min="1" />
                  </el-form-item>
                  <el-form-item label="最多选择">
                    <el-input-number v-model="question.max_choice" :min="1" />
                  </el-form-item>
                </template>
              </template>
            </div>
            <el-button type="primary" @click="addQuestion">添加题目</el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看回答对话框 -->
    <el-dialog
      v-model="answerDialogVisible"
      title="问卷回答"
      width="70%"
    >
      <el-table
        :data="paginatedAnswers"
        style="width: 100%"
        v-loading="answerLoading"
      >
        <el-table-column prop="id" label="ID" width="100" align="center" />
        <el-table-column prop="userId" label="用户ID" width="120" align="center" />
        <el-table-column prop="createtime" label="提交时间" width="200" align="center" />
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleViewAnswerDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="answerCurrentPage"
          v-model:page-size="answerPageSize"
          :total="totalAnswers"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleAnswerSizeChange"
          @current-change="handleAnswerCurrentChange"
        />
      </div>
    </el-dialog>

    <!-- 回答详情对话框 -->
    <el-dialog
      v-model="answerDetailDialogVisible"
      title="回答详情"
      width="50%"
    >
      <div v-if="currentAnswer" class="answer-detail">
        <div v-for="(item, index) in (typeof currentAnswer.content === 'string' ? JSON.parse(currentAnswer.content) : currentAnswer.content)" :key="index" class="answer-item">
          <div class="question-stem">第 {{ item.t_id }} 题</div>
          <div class="answer-content">
            <template v-if="Array.isArray(item.choice)">
              <div v-for="(choice, cIndex) in item.choice" :key="cIndex">
                {{ choice }}
              </div>
            </template>
            <template v-else-if="item.ans">
              {{ item.ans }}
            </template>
            <template v-else>
              {{ item.choice }}
            </template>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import { useStore } from 'vuex'

const store = useStore()
const surveyList = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('创建问卷')
const surveyForm = ref({
  id: null,
  title: '',
  content: []
})

// 回答相关
const answerDialogVisible = ref(false)
const answerDetailDialogVisible = ref(false)
const answerList = ref([])
const answerLoading = ref(false)
const answerCurrentPage = ref(1)
const answerPageSize = ref(10)
const currentAnswer = ref(null)
const currentSurveyId = ref(null)

// 获取当前页的问卷列表
const paginatedSurveys = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return surveyList.value.slice(start, end);
});

// 获取当前页的回答列表
const paginatedAnswers = computed(() => {
  const start = (answerCurrentPage.value - 1) * answerPageSize.value;
  const end = start + answerPageSize.value;
  return answerList.value.slice(start, end);
});

// 计算总问卷数
const totalSurveys = computed(() => surveyList.value.length);

// 计算总回答数
const totalAnswers = computed(() => answerList.value.length);

const fetchSurveys = async () => {
  loading.value = true
  try {
    const response = await axios.get('http://127.0.0.1:3000/admin/surveys/list/', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      },
      headers: {
        Authorization: 'Bearer ' + store.state.user.token
      }
    })
    surveyList.value = response.data.items
  } catch (error) {
    ElMessage.error('获取问卷列表失败')
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  fetchSurveys()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchSurveys()
}

const handleAdd = () => {
  dialogTitle.value = '创建问卷'
  surveyForm.value = {
    id: null,
    title: '',
    content: []
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑问卷'
  surveyForm.value = {
    id: row.id,
    title: row.title,
    content: typeof row.content === 'string' ? JSON.parse(row.content) : row.content
  }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除问卷 ${row.title} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.delete(
        `http://127.0.0.1:3000/admin/surveys/${row.id}/`,
        {
          headers: {
            Authorization: 'Bearer ' + store.state.user.token
          }
        }
      )
      ElMessage.success('问卷删除成功')
      fetchSurveys()
    } catch (error) {
      console.error('删除失败：', error);
      ElMessage.error('删除失败：' + (error.response?.data?.message || error.message));
    }
  })
}

const handleSubmit = async () => {
  try {
    if (!surveyForm.value.title) {
      ElMessage.error('请输入问卷标题');
      return;
    }
    if (surveyForm.value.content.length === 0) {
      ElMessage.error('请添加至少一道题目');
      return;
    }

    const url = surveyForm.value.id 
      ? `http://127.0.0.1:3000/admin/surveys/${surveyForm.value.id}/`
      : 'http://127.0.0.1:3000/admin/surveys/create/';
    
    const method = surveyForm.value.id ? 'put' : 'post';
    
    const requestData = {
      title: surveyForm.value.title,
      content: surveyForm.value.content
    };
    
    console.log('发送的数据:', requestData); // 添加日志
    
    await axios({
      url,
      method,
      data: requestData,
      headers: {
        Authorization: 'Bearer ' + store.state.user.token,
        'Content-Type': 'application/json'
      }
    });

    ElMessage.success(surveyForm.value.id ? '问卷更新成功' : '问卷创建成功');
    dialogVisible.value = false;
    fetchSurveys();
  } catch (error) {
    console.error('操作失败：', error);
    console.error('错误详情：', error.response?.data); // 添加错误详情日志
    ElMessage.error('操作失败：' + (error.response?.data?.message || error.message));
  }
}

const handleViewAnswers = async (row) => {
  currentSurveyId.value = row.id;
  answerCurrentPage.value = 1;
  await fetchAnswers();
  answerDialogVisible.value = true;
}

const fetchAnswers = async () => {
  answerLoading.value = true;
  try {
    const response = await axios.get(
      `http://127.0.0.1:3000/admin/surveys/${currentSurveyId.value}/answers/`,
      {
        params: {
          page: answerCurrentPage.value,
          size: answerPageSize.value
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        }
      }
    );
    answerList.value = response.data.items;
  } catch (error) {
    console.error('获取回答列表失败：', error);
    ElMessage.error('获取回答列表失败：' + (error.response?.data?.message || error.message));
  } finally {
    answerLoading.value = false;
  }
}

const handleViewAnswerDetail = async (row) => {
  try {
    const response = await axios.get(
      `http://127.0.0.1:3000/admin/surveys/answers/${row.id}/`,
      {
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        }
      }
    );
    currentAnswer.value = response.data.answer;
    answerDetailDialogVisible.value = true;
  } catch (error) {
    console.error('获取回答详情失败：', error);
    ElMessage.error('获取回答详情失败：' + (error.response?.data?.message || error.message));
  }
}

const handleAnswerSizeChange = (val) => {
  answerPageSize.value = val;
  answerCurrentPage.value = 1;
  fetchAnswers();
}

const handleAnswerCurrentChange = (val) => {
  answerCurrentPage.value = val;
  fetchAnswers();
}

// 题目编辑相关方法
const addQuestion = () => {
  surveyForm.value.content.push({
    t_id: surveyForm.value.content.length + 1,
    type: 'single',
    required: true,
    question_stem: '',
    choice: ['选项1', '选项2'],
    add_choice: false
  });
}

const removeQuestion = (index) => {
  surveyForm.value.content.splice(index, 1);
  // 重新编号
  surveyForm.value.content.forEach((q, i) => {
    q.t_id = i + 1;
  });
}

const addOption = (questionIndex) => {
  surveyForm.value.content[questionIndex].choice.push(`选项${surveyForm.value.content[questionIndex].choice.length + 1}`);
}

const removeOption = (questionIndex, optionIndex) => {
  surveyForm.value.content[questionIndex].choice.splice(optionIndex, 1);
}

onMounted(() => {
  fetchSurveys();
})
</script>

<style scoped>
.survey-management {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  width: 100%;
  box-sizing: border-box;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  width: 100%;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  width: 100%;
}

.question-editor {
  border: 1px solid #dcdfe6;
  padding: 20px;
  border-radius: 4px;
  width: 100%;
}

.question-item {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  width: 100%;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.option-item {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.answer-detail {
  padding: 20px;
  width: 100%;
}

.answer-item {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  width: 100%;
}

.question-stem {
  font-weight: bold;
  margin-bottom: 10px;
  color: #409eff;
}

.answer-content {
  padding-left: 20px;
}

/* 添加新的样式 */
:deep(.el-table) {
  width: 100% !important;
}

:deep(.el-dialog) {
  width: 80% !important;
  max-width: 1200px;
}

:deep(.el-dialog__body) {
  padding: 20px;
}
</style> 