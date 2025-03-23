import $ from 'jquery'

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
            console.log("CIALLO!");

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
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        // 本地存储：是一个字典
                        localStorage.setItem("jwt_token", resp.token);
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },
        getinfo(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/info/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    // console.log("getinfo 响应数据:", resp); // 打印响应数据
                    if (resp.error_message === "success") {
                        context.commit("updateUser", {
                            ...resp,
                            is_login: true,
                        });
                        if (typeof data.success === 'function') {
                            data.success(resp);
                        }
                    } else {
                        // console.error("getinfo 返回错误:", resp); // 打印错误信息
                        if (typeof data.error === 'function') {
                            data.error(resp);
                        }
                    }
                },
                error(resp) {
                    // console.error("getinfo 请求失败:", resp); // 打印请求错误
                    if (typeof data.error === 'function') {
                        data.error(resp);
                    }
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
