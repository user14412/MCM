<template>
    <ContentField>
        <div v-if="loadding">正在加载 ...</div>
        <div v-else class="survey-container">
            <div class="survey-title">{{ title }}</div>
            <form @submit.prevent="submitSurvey">
                <div v-for="question in content" :key="question.t_id" class="question-item">
                    <!-- 单选题 -->
                    <div v-if="question.type == 'single'" class="question-body single">
                        <div class="question-stem">
                            {{ question.t_id }}. {{ question.question_stem }}（单选）
                            <span v-if="question.required" class="required-mark">*</span>
                        </div>
                        <div class="options-container">
                            <!-- 一般选项 -->
                            <div v-for="(option, index) in question.choice" :key="index" class="form-check">
                                <input class="form-check-input" type="radio" :id="'q_' + question.t_id + '_' + index"
                                    :value="option" v-model="answer[question.t_id].choice"
                                    :required="question.required">
                                <label class="form-check-label" for="'q_' + question.t_id + '_' + index">
                                    {{ option }}
                                </label>
                            </div>
                            <!-- 自定义选项 -->
                            <div v-if="question.add_choice != null && question.add_choice == true" class="form-check">
                                <input class="form-check-input" type="radio" :id="'q_' + question.t_id + '_other'"
                                    value="other" v-model="answer[question.t_id].choice">
                                <label class="form-check-label" :for="'q_' + question.t_id + '_other'">
                                    其他
                                </label>
                                <input class="from-control other-input" type="text"
                                    v-if="answer[question.t_id].choice === 'other'"
                                    v-model="answer[question.t_id].add_choice" placeholder="请输入其他选项">
                            </div>
                        </div>
                    </div>
                    <!-- 多选题 -->
                    <div v-if="question.type == 'multiple'" class="question-body multiple">
                        <div class="question-stem">
                            {{ question.t_id }}. {{ question.question_stem }}
                            <span v-if="question.max_choice != null">（最多可选择{{ question.max_choice }}项）</span>
                            <span v-if="question.min_choice != null">（请至少选择{{ question.min_choice }}项）</span>
                            （多选）
                            <span v-if="question.required" class="required-mark">*</span>
                        </div>
                        <div class="options-container">
                            <!-- 一般选项 -->
                            <div v-for="(option, index) in question.choice" :key="index" class="form-check">
                                <input class="form-check-input" type="checkbox" :id="'q_' + question.t_id + '_' + index"
                                    :value="option" v-model="answer[question.t_id].choice"
                                    @change="validateMultiChoice(question)">
                                <label class="form-check-label" for="'q_' + question.t_id + '_' + index">
                                    {{ option }}
                                </label>
                            </div>
                            <!-- 自定义选项 -->
                            <div v-if="question.add_choice != null && question.add_choice == true" class="form-check">
                                <input class="form-check-input" type="checkbox" :id="'q_' + question.t_id + '_other'"
                                    value="other" v-model="answer[question.t_id].choice"
                                    @change="validateMultiChoice(question)">
                                <label class="form-check-label" :for="'q_' + question.t_id + '_other'">
                                    其他
                                </label>
                                <input class="from-control other-input" type="text"
                                    v-if="Array.isArray(answer[question.t_id].choice) && answer[question.t_id].choice.includes('other')"
                                    v-model="answer[question.t_id].add_choice" placeholder="请输入其他选项">
                            </div>
                        </div>
                    </div>
                    <!-- 简答题 -->
                    <div v-if="question.type == 'text'" class="question-body text">
                        <div class="question-stem">
                            {{ question.t_id }}. {{ question.question_stem }}（简答）
                            <span v-if="question.required" class="required-mark">*</span>
                        </div>
                        <textarea class="form-control" rows="3"
                                v-model="answer[question.t_id].ans"
                                :required="question.required"></textarea>
                    </div>
                </div>
                <div class="submit-container d-grid col-3 mx-auto" style="margin-top:40px; text-align: center;">
                    <!-- <div class=" error-message">{{ feedback.error_message }}</div>
                    <div class=" success-message">{{ feedback.success_message }}</div> -->
                    <button type="submit" class="btn btn-primary">
                        提交
                    </button>
                </div>
            </form>

        </div>
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import { reactive, ref, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useStore } from "vuex";
import axios from "axios";

export default {
    components: {
        ContentField,
    },
    setup() {
        const store = useStore();
        const route = useRoute();
        // ref
        const surveyId = computed(() => {
            return parseInt(route.params.surveyId);
        });

        const loadding = ref(true);
        const title = ref('');
        const content = ref([]);
        const answer = reactive([]);

        // 初始化答案json数组中各json结构
        const initAnswer = (questions) => {
            questions.forEach(question => {
                if (question.type === 'single') {
                    answer[question.t_id] = {
                        t_id: question.t_id,
                        choice: '', // 单选题的 choice 初始化为空字符串
                        add_choice: question.add_choice ? '' : undefined
                    };
                } else if (question.type === 'multiple') {
                    answer[question.t_id] = {
                        t_id: question.t_id,
                        choice: [], // 多选题的 choice 初始化为空数组
                        add_choice: question.add_choice ? '' : undefined
                    };
                } else if (question.type === 'text') {
                    answer[question.t_id] = {
                        t_id: question.t_id,
                        ans: ''
                    };
                }
            });
        }

        // 验证多选题选择数量
        const validateMultiChoice = (question) => {
            // 确保 choice 是一个数组
            if (!Array.isArray(answer[question.t_id].choice)) {
                answer[question.t_id].choice = [];
            }
            // 多选题如果勾的超过了上限，立马弹出最后一个选项并弹窗示意
            if(question.max_choice && answer[question.t_id].choice.length > question.max_choice){
                answer[question.t_id].choice.pop(); // 从选项中弹出一项
                alert(`最多只能选择${question.max_choice}`);
            }
        }

        // 提交问卷
        const submitSurvey = () => {
            // 验证必填项
            for(const question of content.value){
                if(question.type === 'single' && !answer[question.t_id].choice){
                    alert(`请完成第${question.t_id}题`);
                    return;
                }
                if(question.type === 'multiple' && answer[question.t_id].choice.length === 0){
                    alert(`请完成第${question.t_id}题`);
                    return;
                }
                if(question.type === 'text' && !answer[question.t_id].ans.trim()){
                    alert(`请完成第${question.t_id}题`);
                    return;
                }
                if (question.type === 'multiple' && question.min_choice && answer[question.t_id].choice.length < question.min_choice) {
                    alert(`第${question.t_id}题至少选则${question.min_choice}项`);
                    return;
                }
            }

            // 准备提交数据(将所有other替换成对应的用户自定义选项)
            const answerData = Object.values(answer).map(res => {
                if (res.choice === 'other') {
                    return {
                        t_id: res.t_id,
                        choice: res.add_choice,
                        add_choice: res.add_choice
                    };
                }
                if (Array.isArray(res.choice) && res.choice.includes('other')) {
                    return {
                        t_id: res.t_id,
                        choice: res.choice.filter(c => c !== 'other').concat(res.add_choice),
                        add_choice: res.add_choice
                    };
                }
                return res;
            });

            // console.log("提交数据", answerData);
            // 提交数据
            axios({
                url: "http://127.0.0.1:3000/surveyans/add/",
                method: "POST",
                params: {
                    surveyId: surveyId.value, // integer
                    content: window.encodeURI(JSON.stringify(answerData)) // json数组
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
            })
                .then(() => {
                    alert("问卷提交成功")
                })
                .catch((error) => {
                    console.log("error", error);
                    alert("提交失败，请重试")
                });
        }

        // 获取问卷
        const fetchSurvey = () =>{
            axios({
                url: "http://127.0.0.1:3000/survey/get/",
                method: "GET",
                params:{
                    surveyId: surveyId.value,
                },
                headers:{
                    Authorization: "Bearer " + store.state.user.token,
                },
            })
            .then((response) => {
                const resp = response.data;
                // console.log("接到问卷", resp);
                title.value = resp.title;
                content.value = resp.content;
                initAnswer(content.value);
                loadding.value = false;
            })
            .catch((error) => {
                console.log("error", error);
            });
        }
        onMounted(() => {
            fetchSurvey();
        })
        return{
            loadding,
            title,
            content,
            answer,
            submitSurvey,
            validateMultiChoice,
        }
    }
}
</script>

<style scoped>
.survey-title{
    text-align: center;
    color:#333;
    margin-bottom: 30px;
    /* font-family: "SimHei", sans-serif !important; */
    font-weight: 550 !important;
    font-size: 1.5em;
}
.question-stem{
    color:#007bff;
    /* font-weight: bold; */
    margin-bottom: 15px;
}

.question-item {
    margin-bottom:10px;
    padding: 5px;
    background-color: #fff;
    border-radius: 5px;
    /* box-shadow: 0 2px 2px rgba(0, 0, 0, 0.1); */
}

.options-container {
    margin-left: 20px;
}

.form-check {
    margin-bottom: 10px;
}

.other-input {
    margin-top: 5px;
    width: 80%;
    margin-left: 20px;
}

.required-mark {
    color: red;
    margin-left: 5px;
}

.submit-container {
    text-align: center;
    margin-top: 30px;
}
</style>
