<template>
    <div>
        更换头像
        <input type="file" @change="handleFileChange">
        <button @click="uploadFile()" type="button" class="btn btn-primary">
            提交
        </button>
    </div>
</template>

<script>
import { useStore } from "vuex";
import { ref } from 'vue'
import axios from 'axios';

export default {
    setup() {
        const store = useStore();

        const file = ref(null);

        const handleFileChange = (event) => {
            file.value = event.target.files[0];
            console.log(file.value);
        };

        const uploadFile = () => {
            if (!file.value) {
                alert("请上传文件");
                return;
            }
            const formData = new FormData();
            formData.append("file", file.value);
            try {
                console.log(file.value);
                console.log(formData);
                // 在这里实现文件上传逻辑
                axios({
                    url: "http://127.0.0.1:3000/test/",
                    method: "POST",
                    data: formData,
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                        'Content-Type': 'multipart/form-data' // 设置正确的Content-Type
                    },
                })
                    .then((response) => {
                        const resp = response.data;
                        console.log("resp", resp);
                        
                        // 修改头像
                        updateUserPhoto(resp);
                    })
                    .catch((error) => {
                        console.log("error", error);
                    });
                // alert("上传成功");
            } catch (error) {
                console.error(error);
            }
        };

        const updateUserPhoto = (imageUrl) => {
            axios({
                url: "http://127.0.0.1:3000/user/photo/update/",
                method: "POST",
                params:{
                    imageUrl: imageUrl,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                }
            })
                .then((response) => {
                    const resp = response.data;
                    if (resp === "success") {
                        alert("头像修改成功，请刷新查看");
                    } else {
                        alert("头像修改失败，请重试");
                    }

                })
                .catch((error) => {
                    console.log("error", error);
                })
                
        }

        return {
            file,
            handleFileChange,
            uploadFile,
            updateUserPhoto,
        };
    }
}
</script>

<style scoped></style>
