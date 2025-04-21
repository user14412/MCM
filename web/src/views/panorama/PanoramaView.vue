<template>
    <div class="panorama-container d-flex justify-content-center align-items-center min-vh-100">
        <div id="panorama">
            <div class="visit-count">访问次数: {{ visitCount }}</div>
            <div class="bgm-control ctrl" @click="toggleBgm" :title="isBgmPlaying ? '暂停背景音乐' : '播放背景音乐'">
                {{ isBgmPlaying ? '&#9208;' : '&#9654;' }}
            </div>
            <div id="controls">
                <div class="ctrl" id="pan-up">&#9650;</div>
                <div class="ctrl" id="pan-down">&#9660;</div>
                <div class="ctrl" id="pan-left">&#9664;</div>
                <div class="ctrl" id="pan-right">&#9654;</div>
                <div class="ctrl" id="zoom-in">&plus;</div>
                <div class="ctrl" id="zoom-out">&minus;</div>
                <div class="ctrl" id="fullscreen">&#x2922;</div>
            </div>
        </div>
    </div>
</template>

<script>
import { onMounted, ref, onUnmounted } from 'vue';
import { useRoute } from 'vue-router'; // 导入 useRoute
import axios from 'axios'; // 导入 axios
import { useStore } from 'vuex';

export default {
    setup() {
        const route = useRoute(); // 获取当前路由信息
        const store = useStore();
        const sceneId = route.params.sceneId || 'scene1'; // 从路由参数获取 sceneId，提供默认值
        const visitCount = ref(0); // 添加 ref 来存储访问次数
        const bgmAudio = ref(null); // 背景音乐 Audio 对象
        const isBgmPlaying = ref(false); // 背景音乐播放状态

        let currentAudio = ref(null); // 使用 ref 来跟踪当前播放的音频

        // 热点点击处理函数
        const handleInfoHotspotClick = (event, args) => {
            const audioUrl = args.audioUrl;
            if (!audioUrl) return;

            if (currentAudio.value) {
                // 如果当前有音频在播放
                const previousAudioSrc = new URL(currentAudio.value.src).pathname;
                const clickedAudioSrc = new URL(audioUrl, window.location.href).pathname;

                currentAudio.value.pause(); // 暂停当前音频
                currentAudio.value.currentTime = 0; // 重置播放时间

                if (previousAudioSrc === clickedAudioSrc) {
                    // 如果点击的是同一个热点，则停止播放并清除引用
                    currentAudio.value = null;
                    return;
                }
            }

            // 创建新的 Audio 对象并播放
            const audio = new Audio(audioUrl);
            currentAudio.value = audio; // 更新当前播放的音频引用
            audio.play();

            // 监听播放结束事件，结束后清除引用
            audio.addEventListener('ended', () => {
                if (currentAudio.value === audio) {
                     currentAudio.value = null;
                }
            }, { once: true }); // 使用 once 选项确保监听器只执行一次
        };

        // 切换背景音乐播放/暂停
        const toggleBgm = () => {
            console.log("Toggling background music.");
            if (!bgmAudio.value){
                console.log("Background music is not initialized.");
                return; // 如果背景音乐为空，则返回
            }
                if (isBgmPlaying.value) {
                bgmAudio.value.pause();
            } else {
                bgmAudio.value.play();
            }
            // 状态会在 audio 元素的 onplay/onpause 事件中更新
        };

        const initPannellum = () => {
            const viewer = window.pannellum.viewer('panorama', {
                "default": {
                    "autoLoad": true, // 自动加载
                    "firstScene": `${sceneId}`, // 使用从路由获取的 sceneId
                    "author": "zanyyan123",
                    "sceneFadeDuration": 1000, // 场景过渡1s
                    "showControls": false,
                },
                "scenes": {
                    "scene1": { // sceneId
                        "title": "远古海洋",
                        "hfov": 110,
                        "pitch": -3,
                        "yaw": 117,
                        "type": "equirectangular",
                        "panorama": "https://i.imgur.com/PhBlDDt.jpeg",
                        "hotSpots": [
                            {
                                "pitch": 0, // 正数代表向上的角度
                                "yaw": 30, // 证书代表向右的角度
                                "type": "scene",
                                "text": "近代海洋",
                                "sceneId": "scene2"
                            },
                            {
                                "pitch": 0, // 正数代表向上的角度
                                "yaw": -30, // 证书代表向右的角度
                                "type": "scene",
                                "text": "现代海洋",
                                "sceneId": "scene3"
                            },
                            {
                                "pitch": 0,
                                "yaw": 150,
                                "type": "info", // 提供信息
                                "text": `贝类化石(点击听取介绍)
                                    远古贝类化石是3.9亿年前中泥盆世海洋生物的遗存，如鸮头贝壳体浑圆、腹喙如钩，壳面可见同心生长线；生蚝化石因碳酸钙易分解，需极端地质条件才能保存，壳体常呈梯田状分层。这类化石多存于泥灰岩中，通过壳体结构可还原古海洋温度、盐度等环境信息，其稀有性和完整度使其兼具科研与收藏价值。
                                `,
                                "targetYaw": -23,
                                "targetPitch": 2,
                                "audioUrl": require("@/assets/audio/test_audio.mp3"),
                                "clickHandlerFunc": handleInfoHotspotClick,
                                "clickHandlerArgs": { "audioUrl": require("@/assets/audio/test_audio.mp3") }
                            },
                        ]
                    },

                    "scene2": {
                        "title": "近代海洋",
                        "hfov": 110,
                        "yaw": 5,
                        "type": "equirectangular",
                        "panorama": "https://i.imgur.com/m8XsNrp.jpeg",
                        "hotSpots": [
                            {
                                "pitch": -5,
                                "yaw": -28,
                                "type": "scene", // 场景切换
                                "text": "远古海洋",
                                "sceneId": 'scene1',
                                "targetYaw": -23,
                                "targetPitch": 2
                            },
                        ]
                    },
                    "scene3": {
                        "title": "现代海洋",
                        "hfov": 0,
                        "yaw": 0,
                        "type": "equirectangular",
                        "panorama": "https://i.imgur.com/PhBlDDt.jpeg",
                        "hotSpots": [
                            {
                                "pitch": 0, // 正数代表向上的角度
                                "yaw": 30, // 证书代表向右的角度
                                "type": "scene",
                                "text": "远古海洋",
                                "sceneId": "scene1"
                            },
                            {
                                "pitch": 0,
                                "yaw": 0,
                                "type": "info", // 提供信息
                                "text": `海螺化石(点击听取介绍)
                                    海螺贝类化石是3.9亿年前中泥盆世海洋生物的遗存，如鸮头贝壳体浑圆、腹喙如钩，壳面可见同心生长线；生蚝化石因碳酸钙易分解，需极端地质条件才能保存，壳体常呈梯田状分层。这类化石多存于泥灰岩中，通过壳体结构可还原古海洋温度、盐度等环境信息，其稀有性和完整度使其兼具科研与收藏价值。
                                `,
                                "targetYaw": -23,
                                "targetPitch": 2,
                                "audioUrl": "http://music.163.com/song/media/outer/url?id=447925558.mp3",
                                "clickHandlerFunc": handleInfoHotspotClick,
                                "clickHandlerArgs": { "audioUrl": "http://music.163.com/song/media/outer/url?id=447925558.mp3" }
                            },
                        ]
                    },

                }
            });

            // 在初始化后立即触发全屏
            viewer.toggleFullscreen();

            // Make buttons work
            document.getElementById('pan-up').addEventListener('click', function () {
                viewer.setPitch(viewer.getPitch() + 10);
            });
            document.getElementById('pan-down').addEventListener('click', function () {
                viewer.setPitch(viewer.getPitch() - 10);
            });
            document.getElementById('pan-left').addEventListener('click', function () {
                viewer.setYaw(viewer.getYaw() - 10);
            });
            document.getElementById('pan-right').addEventListener('click', function () {
                viewer.setYaw(viewer.getYaw() + 10);
            });
            document.getElementById('zoom-in').addEventListener('click', function () {
                viewer.setHfov(viewer.getHfov() - 10);
            });
            document.getElementById('zoom-out').addEventListener('click', function () {
                viewer.setHfov(viewer.getHfov() + 10);
            });
            document.getElementById('fullscreen').addEventListener('click', function () {
                viewer.toggleFullscreen();
            });
        }

        onMounted(() => {
            const scriptUrl = 'https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.js';
            const cssUrl = 'https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.css';

            // 检查 CSS 是否已加载
            if (!document.querySelector(`link[href="${cssUrl}"]`)) {
                const link = document.createElement('link');
                link.rel = 'stylesheet';
                link.href = cssUrl;
                document.head.appendChild(link);
            }

            // 检查 Pannellum 脚本是否已加载
            if (typeof window.pannellum !== 'undefined') {
                initPannellum();
            } else {
                const script = document.createElement('script');
                script.src = scriptUrl;
                script.onload = () => {
                    // 确保在脚本加载后调用在 setup 中定义的 initPannellum
                    initPannellum();
                };
                document.head.appendChild(script);
            }

            // 记录访问次数
            axios({
                url:'http://127.0.0.1:3000/stat/visit/panorama/',
                method:'POST',
                params:{
                    page: 'panorama',
                },
                headers:{
                    Authorization: "Bearer " + store.state.user.token,
                },
            })
                .then(response => {
                    console.log('Visit recorded:', response.data);
                })
                .catch(error => {
                    console.error('Error recording visit:', error);
                })

            // 获取访问次数
            axios({
                url:'http://127.0.0.1:3000/stat/count/panorama/',
                method:'POST', // 按照用户提供的接口
                headers:{
                    Authorization: "Bearer " + store.state.user.token,
                },
            })
                .then(response => {
                    visitCount.value = response.data; // 更新访问次数
                    console.log('Visit count fetched:', response.data);
                })
                .catch(error => {
                    console.error('Error fetching visit count:', error);
                })

            // --- 初始化背景音乐 ---
            // 请将 'YOUR_BGM_AUDIO_URL_HERE' 替换为你的背景音乐文件URL
            const bgmUrl = require('@/assets/audio/Clair de Lune.mp3');
            if (bgmUrl && bgmUrl !== 'YOUR_BGM_AUDIO_URL_HERE') { // 确保 URL 有效且不是占位符
               try {
                   bgmAudio.value = new Audio(bgmUrl);
                   bgmAudio.value.loop = true;
                   bgmAudio.value.volume = 0.3; // 可以设置一个合适的初始音量

                   // 尝试自动播放
                   bgmAudio.value.play().then(() => {
                       isBgmPlaying.value = true;
                       console.log("Background music started playing.");
                   }).catch(error => {
                       console.warn("Background music autoplay failed:", error);
                       // 用户可能需要手动点击播放按钮启动音乐
                       isBgmPlaying.value = false;
                   });

                   // 监听播放和暂停事件以同步状态
                   bgmAudio.value.onplay = () => {
                        console.log("Background music started playing.");
                        isBgmPlaying.value = true;
                    };
                   bgmAudio.value.onpause = () => {
                        console.log("Background music paused.");
                        isBgmPlaying.value = false;
                    };
               } catch (e) {
                   console.error("Error initializing background music:", e);
               }
            } else {
               console.warn("Background music URL is not set or is invalid.");
            }
            // --- 背景音乐初始化结束 ---
        })

        // Hot spot creation function
        function hotspot(hotSpotDiv, args) {
            hotSpotDiv.classList.add('custom-tooltip');
            var span = document.createElement('span');
            span.innerHTML = args;
            hotSpotDiv.appendChild(span);
            span.style.width = span.scrollWidth - 20 + 'px';
            span.style.marginLeft = -(span.scrollWidth - hotSpotDiv.offsetWidth) / 2 + 'px';
            span.style.marginTop = -span.scrollHeight - 12 + 'px';
        }

        // 组件卸载时清理背景音乐
        onUnmounted(() => {
            if (bgmAudio.value) {
                bgmAudio.value.pause();
                bgmAudio.value.onplay = null;
                bgmAudio.value.onpause = null;
                bgmAudio.value.src = ''; // 释放资源
                bgmAudio.value = null;
                console.log("Background music resources released.");
            }
        });

        return {
            hotspot,
            visitCount, // 返回 visitCount 使其在模板中可用
            isBgmPlaying, // 返回背景音乐播放状态
            toggleBgm,    // 返回切换函数
        }
    }
}
</script>

<style scoped>
#panorama {
    width: 90vw;
    height: 90vh;
    position: relative; /* 添加相对定位以便绝对定位子元素 */
}

/* 访问次数显示样式 */
.visit-count {
    position: absolute;
    top: 10px;
    left: 10px;
    background-color: rgba(0, 0, 0, 0.5);
    color: white;
    padding: 5px 10px;
    border-radius: 5px;
    z-index: 3; /* 确保在控制栏之上 */
    font-size: 14px;
}

/* 背景音乐控制按钮样式 */
.bgm-control {
    position: absolute;
    left: 10px; /* 距离左侧距离 */
    top: 50%;   /* 垂直居中 */
    transform: translateY(-50%); /* 精确垂直居中 */
    z-index: 3; /* 确保在访问次数和其他控件之上 */
    cursor: pointer;
    background: rgba(0, 0, 0, 0.5); /* 半透明背景 */
    color: white;
    border-radius: 50%; /* 圆形按钮 */
    width: 35px;  /* 按钮宽度 */
    height: 35px; /* 按钮高度 */
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 18px; /* 图标大小 */
    line-height: 1; /* 避免额外行高影响居中 */
    border: 1px solid rgba(255, 255, 255, 0.5); /* 可选：添加边框 */
    box-shadow: 0 2px 4px rgba(0,0,0,0.2); /* 可选：添加阴影 */
    transition: background-color 0.2s ease; /* 平滑过渡效果 */
}

.bgm-control:hover {
    background: rgba(0, 0, 0, 0.7); /* 悬停时加深背景 */
}

/* 控制栏 */
#controls {
    position: absolute;
    bottom: 0;
    z-index: 2;
    text-align: center;
    width: 100%;
    padding-bottom: 3px;
    pointer-events: none;
}

.ctrl {
    padding: 8px 5px;
    width: 30px;
    text-align: center;
    background: rgba(200, 200, 200, 0.8);
    display: inline-block;
    cursor: pointer;
    pointer-events: auto;
}

.ctrl:hover {
    background: rgba(200, 200, 200, 1);
}

/* 热点 */
/* .custom-hotspot {
    height: 50px;
    width: 50px;
    background: #f00;
}

div.custom-tooltip span {
    visibility: hidden;
    position: absolute;
    border-radius: 3px;
    background-color: #fff;
    color: #000;
    text-align: center;
    max-width: 200px;
    padding: 5px 10px;
    margin-left: -220px;
    cursor: default;
}

div.custom-tooltip:hover span {
    visibility: visible;
}

div.custom-tooltip:hover span:after {
    content: '';
    position: absolute;
    width: 0;
    height: 0;
    border-width: 10px;
    border-style: solid;
    border-color: #fff transparent transparent transparent;
    bottom: -20px;
    left: -10px;
    margin: 0 50%;
} */
</style>