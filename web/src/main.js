import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/assets/iconfont/iconfont.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const app = createApp(App)
app.use(store).use(router).mount('#app')

document.title = 'HEU海洋文化馆官网';
