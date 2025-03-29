import axios from "axios";

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
        pulling_info: false, // 正在拉取信息，不展示登录界面
    },
    getters: {
    },
    mutations: {
        // mutation 直译为“变异”
        // 修改state的唯一方式
        // 同步，提供一个字符串函数名和一个回调函数用于修改state
        // commit方式调用
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
        logout(state) {

            state.id = "";
            state.username = "";
            state.photo = "";
            state.token = "";
            state.is_login = false;
        },
        updatePullingInfo(state, pulling_info) {
            state.pulling_info = pulling_info;
        }
    },
    actions: {
        // 异步，提交mutation用于修改state
        // dispatch方式调用
        login(context, data) {
            axios.post("http://127.0.0.1:3000/user/account/token/", {
                username: data.username,
                password: data.password
            })
                .then(resp => {
                    const response = resp.data; // Axios响应数据在data属性中[1,5](@ref)
                    if (response.error_message === "success") {
                        localStorage.setItem("jwt_token", response.token);
                        context.commit("updateToken", response.token);
                        data.success(response);
                    } else {
                        data.error(response);
                    }
                })
                .catch(error => {
                    // 处理网络错误或服务器错误响应
                    const errorResponse = error.response?.data || error.message;
                    data.error(errorResponse);
                });
        },
        getinfo(context, data) {
            axios.get("http://127.0.0.1:3000/user/account/info/", {
                headers: {
                    Authorization: "Bearer " + context.state.token
                }
            })
                .then(resp => {
                    console.log("getinfo 响应数据:", resp.data); // 打印响应数据
                    if (resp.data.error_message === "success") {
                        context.commit("updateUser", {
                            ...resp.data,
                            is_login: true,
                        });
                        if (typeof data.success === 'function') {
                            data.success(resp.data);
                        }
                    } else {
                        console.error("getinfo 返回错误:", resp.data); // 打印错误信息
                        if (typeof data.error === 'function') {
                            data.error(resp.data);
                        }
                    }
                })
                .catch(error => {
                    console.error("getinfo 请求失败:", error); // 打印请求错误
                    if (typeof data.error === 'function') {
                        data.error(error);
                    }
                });
        },
        logout(context) {
            localStorage.removeItem("jwt_token");
            context.commit("logout");
        }
    },
    modules: {
    }
}
