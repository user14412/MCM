<template>
    <div class="panorama-container d-flex justify-content-center align-items-center min-vh-100">
        <div id="panorama">
            <div class="visit-count">访问次数: {{ visitCount }}</div>
            <div id="controls">
                <div class="ctrl" id="pan-up">&#9650;</div>
                <div class="ctrl" id="pan-down">&#9660;</div>
                <div class="ctrl" id="pan-left">&#9664;</div>
                <div class="ctrl" id="pan-right">&#9654;</div>
                <div class="ctrl" id="zoom-in">&plus;</div>
                <div class="ctrl" id="zoom-out">&minus;</div>
                <div class="ctrl" id="fullscreen">&#x2922;</div>
            </div>
            
            <!-- 留言弹幕区 -->
            <div class="barrage-container">
                <transition-group name="scroll-fade" tag="div" v-if="showMessages">
                    <div v-for="message in visibleMessages" :key="message.id" class="barrage-item">
                        <span class="barrage-content">{{ message.content }}</span>
                    </div>
                </transition-group>
                <!-- 输入区 -->
                <div class="barrage-input-row">
                    <input v-model="newMessage" type="text" class="barrage-input" placeholder="在此输入您的留言" :maxlength="2000" @keyup.enter="submitMessage" />
                    <button class="barrage-send" @click="submitMessage" :disabled="!newMessage.trim()">发送</button>
                    <span class="barrage-toggle" :title="showMessages ? '关闭留言' : '开启留言'" @click="toggleMessages">
                        <svg v-if="showMessages" width="22" height="22" viewBox="0 0 1024 1024"><path d="M512 64C264.6 64 64 235.8 64 448c0 97.2 53.2 185.2 140.2 247.2-4.2 31.2-17.2 77.2-54.2 120.8-6.2 7.2-7.2 17.2-2.2 25.2 5 8.2 14.8 11.2 23.2 7.8 70.2-28.2 124.2-67.2 159.2-97.2 27.2 6.2 55.8 9.2 85.8 9.2 247.4 0 448-171.8 448-384S759.4 64 512 64z" fill="#bbb"/></svg>
                        <svg v-else width="22" height="22" viewBox="0 0 1024 1024"><path d="M512 64C264.6 64 64 235.8 64 448c0 97.2 53.2 185.2 140.2 247.2-4.2 31.2-17.2 77.2-54.2 120.8-6.2 7.2-7.2 17.2-2.2 25.2 5 8.2 14.8 11.2 23.2 7.8 70.2-28.2 124.2-67.2 159.2-97.2 27.2 6.2 55.8 9.2 85.8 9.2 247.4 0 448-171.8 448-384S759.4 64 512 64z" fill="#eee"/></svg>
                    </span>
                    <span class="barrage-music" :title="isBgmPlaying ? '暂停背景音乐' : '播放背景音乐'" @click="toggleBgm">
                        <svg v-if="isBgmPlaying" width="22" height="22" viewBox="0 0 48 48">
                            <circle cx="24" cy="24" r="22" fill="#eee"/>
                            <rect x="14" y="14" width="6" height="20" rx="2" fill="#333"/>
                            <rect x="28" y="14" width="6" height="20" rx="2" fill="#333"/>
                        </svg>
                        <svg v-else width="22" height="22" viewBox="0 0 48 48">
                            <circle cx="24" cy="24" r="22" fill="#eee"/>
                            <polygon points="17,14 36,24 17,34" fill="#333"/>
                        </svg>
                    </span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { onMounted, ref, onUnmounted, computed } from 'vue';
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
        const showMessages = ref(true);
        const messages = ref([]);
        const newMessage = ref('');
        const messageInterval = ref(null);
        const scrollIndex = ref(0);
        const visibleCount = 5; // 一次显示5条
        let scrollTimer = null;

        let currentAudio = ref(null); // 使用 ref 来跟踪当前播放的音频

        // 自动滚动可见留言
        const visibleMessages = computed(() => {
            if (messages.value.length <= visibleCount) {
                return messages.value;
            }
            let start = scrollIndex.value;
            let end = start + visibleCount;
            if (end <= messages.value.length) {
                return messages.value.slice(start, end);
            } else {
                // 拼接头部
                return messages.value.slice(start).concat(messages.value.slice(0, end - messages.value.length));
            }
        });

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

        // 切换弹幕显示
        const toggleMessages = () => {
            showMessages.value = !showMessages.value;
        };

        // 格式化时间
        const formatTime = (timeStr) => {
            const date = new Date(timeStr);
            return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
        };

        // 获取留言列表
        const fetchMessages = async () => {
            console.log("Fetching messages...");
            try {
                const response = await axios({
                    url: 'http://127.0.0.1:3000/panorama/message/get/',
                    method: 'GET',
                    params: {
                        scene: sceneId
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    }
                });
                if (response.data.error_message === 'success') {
                    messages.value = response.data.messages;
                    // 重置滚动索引，防止新留言时错位
                    scrollIndex.value = 0;
                }
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        };

        // 提交新留言
        const submitMessage = async () => {
            console.log("Submitting message...");
            if (!newMessage.value.trim()) return;
            
            try {
                const response = await axios({
                    url: 'http://127.0.0.1:3000/panorama/message/add/',
                    method: 'POST',
                    params: {
                        content: newMessage.value,
                        scene: sceneId
                    },
                    headers: {
                        Authorization: "Bearer " + store.state.user.token,
                    }
                });
                
                if (response.data.error_message === 'success') {
                    newMessage.value = '';
                    fetchMessages(); // 刷新留言列表
                    window.alert('留言发送成功，正在审核');
                }
            } catch (error) {
                console.error('Error submitting message:', error);
            }
        };

        const initPannellum = () => {
            const viewer = window.pannellum.viewer('panorama', {
                "default": {
                    "autoLoad": true, // 自动加载
                    "firstScene": `${sceneId}`, // 使用从路由获取的 sceneId
                    // "author": "zanyyan123",
                    "sceneFadeDuration": 1000, // 场景过渡1s
                    "showControls": false,
                },
                "scenes": {
                    "scene1": { // sceneId
                        // "title": "远古海洋",
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
                        // "title": "近代海洋",
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
                        // "title": "现代海洋",
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

            fetchMessages();
            // 每30秒刷新一次留言
            messageInterval.value = setInterval(fetchMessages, 30000);
            // 自动滚动留言
            scrollTimer = setInterval(() => {
                if (messages.value.length > visibleCount) {
                    scrollIndex.value = (scrollIndex.value + 1) % messages.value.length;
                }
            }, 2000);
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
            if (messageInterval.value) {
                clearInterval(messageInterval.value);
            }
            if (scrollTimer) clearInterval(scrollTimer);
        });

        return {
            hotspot,
            visitCount, // 返回 visitCount 使其在模板中可用
            isBgmPlaying, // 返回背景音乐播放状态
            toggleBgm,    // 返回切换函数
            showMessages,
            messages,
            newMessage,
            toggleMessages,
            submitMessage,
            formatTime,
            visibleMessages
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

/* 留言弹幕区样式 */
.barrage-container {
    position: absolute;
    left: 20px;
    bottom: 30px;
    width: 320px;
    z-index: 1000;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.barrage-item {
    margin-bottom: 8px;
    display: flex;
    align-items: center;
    background: rgba(40, 40, 40, 0.7);
    color: #fff;
    border-radius: 20px;
    padding: 6px 18px;
    font-size: 18px;
    max-width: 90%;
    word-break: break-all;
    box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.barrage-content {
    display: block;
}

/* 输入区样式 */
.barrage-input-row {
    display: flex;
    align-items: center;
    width: 100%;
    background: rgba(40, 40, 40, 0.5);
    border-radius: 22px;
    padding: 4px 10px 4px 16px;
    margin-top: 8px;
}

.barrage-input {
    flex: 1;
    border: none;
    outline: none;
    background: transparent;
    color: #fff;
    font-size: 16px;
    height: 32px;
    line-height: 32px;
    padding: 0;
}

.barrage-send {
    background: #0068ff;
    color: #fff;
    border: none;
    border-radius: 16px;
    padding: 0 12px;
    height: 32px;
    font-size: 14px;
    cursor: pointer;
    transition: background 0.2s;
    display: flex;
    align-items: center;
    justify-content: center;
    box-sizing: border-box;
    min-width: 40px;
}
.barrage-send:disabled {
    background: #aaa;
    cursor: not-allowed;
}

.barrage-music {
    margin-left: 10px;
    cursor: pointer;
    display: flex;
    align-items: center;
    height: 32px;
    width: 32px;
    border-radius: 50%;
    justify-content: center;
    background: rgba(0,0,0,0.10);
    transition: background 0.2s;
}
.barrage-music:hover {
    background: rgba(0,104,255,0.15);
}

.barrage-toggle {
    margin-left: 10px;
    cursor: pointer;
    display: flex;
    align-items: center;
    height: 32px;
    width: 32px;
    border-radius: 50%;
    justify-content: center;
    transition: background 0.2s;
}
.barrage-toggle:hover {
    background: rgba(255,255,255,0.1);
}

.scroll-fade-enter-active, .scroll-fade-leave-active {
  transition: all 0.5s;
}
.scroll-fade-enter-from, .scroll-fade-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
</style>