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
        const initPannellum = () => {
            const viewer = window.pannellum.viewer('panorama', {
                type: 'equirectangular',
                panorama: require('@/assets/panorama/sphere.jpg'),
                "autoLoad": true, // 自动加载
                "autoRotate": -2, // 自动旋转：每秒顺时针旋转2°
                // "preview": "@/assets/images/shiroha.jpg", // 设置预览图片
                "title": "山谷", // 设置标题
                "author": "Yan Zan", // 设置作者
                // "northOffset": 247.5, // 设置指南针
                // "pitch": 2.3, // 设置初始视图
                // "yaw": -135.4, // 同上
                // "hfov": 120, // 同上
                "showControls": false,
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
    }
}
</script>

<style scoped>
#panorama {
    width: 1200px;
    height: 800px;
}
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
</style>