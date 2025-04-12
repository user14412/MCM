<template>
    <ContentField>
        <div class="form-floating">
            <textarea v-model="feedback.content" class="form-control" style="height: 100px; min-width: 100%; min-height: 400px;"></textarea>
            <label for="floatingTextarea2">请输入您的宝贵意见：</label>
        </div>
        <div class="form-floating" style="margin-top:30px">
            <textarea v-model="feedback.contact_info" class="form-control" style="height: 100px"></textarea>
            <label for="floatingTextarea2">请填写您的联系方式（选填）：</label>
        </div>
        <div class="d-grid col-3 mx-auto" style="margin-top:40px; text-align: center;">
            <div class=" error-message">{{ feedback.error_message }}</div>
            <div class=" success-message">{{ feedback.success_message }}</div>
            <button @click="submit()" type="button" class="btn btn-primary">
                提交
            </button>
        </div>
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import { reactive } from "vue";
import axios from "axios";
import { useStore } from "vuex";

export default {
    components: {
        ContentField,
    },
    setup(){
        const store = useStore();
        const feedback = reactive({
            content: "",
            contact_info: "",
            success_message: "",
            error_message: "",
        })

        const submit = () => {
            // 清除提示信息
            feedback.success_message = "";
            feedback.error_message = "";
            // 向后端发送请求
            axios({
                url: "http://127.0.0.1:3000/feedback/add/",
                method: "POST",
                params:{
                    content: feedback.content,
                    contact_info: feedback.contact_info,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
            })
            .then(response => {
                const resp = response.data; // resp是String
                if(resp === "success"){
                    feedback.success_message = "提交成功！"
                }else{
                    feedback.error_message = resp
                }
            })
            .catch(error => {
                console.error("error: ", error);
            });
        }

        return {
            feedback,
            submit,
        }
    }
}
</script>

<style scoped>
div.error-message {
    color: red;
}
div.success-message {
    color: green;
}
</style>
