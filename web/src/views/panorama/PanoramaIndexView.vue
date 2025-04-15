<template>
    <div class="panorama-containner d-flex justify-content-center align-items-center min-vh-100">
        <div id="panorama">
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
import { onMounted } from 'vue';

export default{
    setup(){
        const firstScene = 'outside';
        const initPannellum = () => {
            const viewer = window.pannellum.viewer('panorama', {
                "default": {
                    "autoLoad": true, // 自动加载
                    "firstScene": `${firstScene}`,
                    "author": "zanyyan123",
                    "sceneFadeDuration": 1000, // 场景过渡1s
                    "showControls": false,
                },
                "scenes": {
                    "inside": { // sceneId
                        "title": "画家的工作室",
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
                                "text": "荒原秘境",
                                "sceneId": "outside"
                            },
                            {
                                "pitch": 0,
                                "yaw": 150,
                                "type": "info", // 提供信息
                                "text": "画家的画",
                                "targetYaw": -23,
                                "targetPitch": 2
                            },
                        ]
                    },

                    "outside": {
                        "title": "荒原秘境",
                        "hfov": 110,
                        "yaw": 5,
                        "type": "equirectangular",
                        "panorama": "https://i.imgur.com/m8XsNrp.jpeg",
                        "hotSpots": [
                            {
                                "pitch": -5,
                                "yaw": -28,
                                "type": "scene", // 场景切换
                                "text": "画家的工作室",
                                "sceneId": 'inside',
                                "targetYaw": -23,
                                "targetPitch": 2
                            },
                        ]
                    }
                }
            });

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
            // 确保脚本加载完成后初始化
            if(typeof window.pannellum != 'undefined'){
                initPannellum();
            }else{
                const script = document.createElement('script')
                script.src = 'https://cdn.jsdelivr.net/npm/pannellum@2.5.6/build/pannellum.js'
                script.onload = () => this.initPannellum();
                document.head.appendChild(script);
            }
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
        
        return {
            hotspot,
        }
    }
}
</script>

<style scoped>
#panorama {
    width: 1200px;
    height: 800px;
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