图片示例：

```html
      <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>图片示例</title>
      <style>
        img {
          max-width: 100%;
          height: auto;
          border: 2px solid #ccc;
          border-radius: 10px;
        }
      </style>
      </head>
      <body>
        <div style="text-align: center;"> 
          <a href="https://baidu.com">
            <img class="fit-picture"
            src="https://upload-os-bbs.hoyolab.com/upload/2023/10/28/152636517/bfc22b670c44ad6fa981bacd1cb69d00_6084162988149067877.jpg?x-oss-process=image%2Fresize%2Cs_1000%2Fauto-orient%2C0%2Finterlace%2C1%2Fformat%2Cwebp%2Fquality%2Cq_70"
            alt="Grapefruit slice atop a pile of other slices"
            width=30% height=30% 
            loading="lazy"/>
          </a>
        </div>
      </body>
```

在HTML文章中编写图片时，需要注意以下标签、格式和最佳实践，以确保图片正确显示并优化用户体验：

---

### 1. **使用`<img>`标签**
`<img>`标签是HTML中用于插入图片的标准标签。它的基本语法如下：
```html
<img src="image.jpg" alt="描述文字">
```


- **`src`属性**：指定图片的路径（可以是相对路径、绝对路径或URL）。
- **`alt`属性**：为图片提供替代文本，当图片无法加载时显示，同时有助于SEO和无障碍访问。

---

### 2. **图片路径**
- **相对路径**：相对于当前HTML文件的位置。
  ```html
  <img src="images/photo.jpg" alt="示例图片">
  ```

- **绝对路径**：从根目录开始的完整路径。
  ```html
  <img src="/images/photo.jpg" alt="示例图片">
  ```

- **URL**：直接引用网络上的图片。
  ```html
  <img src="https://example.com/image.jpg" alt="示例图片">
  ```


---

### 3. **图片格式**
选择适合的图片格式可以优化加载速度和显示效果：
- **JPEG（.jpg/.jpeg）** ：适合照片和复杂图像，文件较小，但不支持透明背景。
- **PNG（.png）** ：适合需要透明背景的图像，文件较大。
- **GIF（.gif）** ：适合简单动画，支持透明背景，但颜色较少。
- **WebP（.webp）** ：现代格式，支持高压缩率和透明背景，兼容性较好。
- **SVG（.svg）** ：矢量格式，适合图标和简单图形，缩放不失真。

---

### 4. **图片尺寸**
- **指定宽度和高度**：使用`width`和`height`属性可以控制图片的显示尺寸。
  ```html
  <img src="image.jpg" alt="示例图片" width="300" height="200">
  ```

- **响应式图片**：使用CSS或`srcset`属性实现图片在不同设备上的自适应。
  ```html
  <img src="image.jpg" alt="示例图片" srcset="image-small.jpg 480w, image-medium.jpg 800w, image-large.jpg 1200w" sizes="(max-width: 600px) 480px, 800px">
  ```


---

### 5. **图片标题**
使用`<figure>`和`<figcaption>`标签为图片添加标题或说明。
```html
<figure>
  <img src="image.jpg" alt="示例图片">
  <figcaption>这是一张示例图片。</figcaption>
</figure>
```


---

### 6. **图片懒加载**
使用`loading="lazy"`属性实现图片懒加载，优化页面性能。
```html
<img src="image.jpg" alt="示例图片" loading="lazy">
```


---

### 7. **图片优化**
- **压缩图片**：使用工具（如TinyPNG、ImageOptim）压缩图片，减少文件大小。
- **使用CDN**：将图片托管在CDN上，加快加载速度。
- **缓存策略**：通过HTTP缓存头（如`Cache-Control`）优化图片加载。

---

### 8. **无障碍访问**
- **`alt`属性**：为所有图片提供有意义的替代文本。
- **`aria-label`或`aria-labelledby`**：为复杂图片提供额外的描述。
  ```html
  <img src="chart.png" alt="销售数据图表" aria-labelledby="chart-desc">
  <p id="chart-desc">2023年销售数据图表，显示逐月增长趋势。</p>
  ```


---

### 9. **图片链接**
将图片包裹在`<a>`标签中，使其可点击。
```html
<a href="https://example.com">
  <img src="image.jpg" alt="示例图片">
</a>
```


---

### 10. **图片与CSS**
- **使用CSS控制样式**：通过CSS设置图片的边框、阴影、圆角等效果。
  ```css
  img {
    border: 2px solid #ccc;
    border-radius: 10px;
    box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.3);
  }
  ```

- **响应式图片**：使用CSS实现图片的自适应。
  ```css
  img {
    max-width: 100%;
    height: auto;
  }
  ```


---

### 示例代码
```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>图片示例</title>
  <style>
    img {
      max-width: 100%;
      height: auto;
      border: 2px solid #ccc;
      border-radius: 10px;
    }
  </style>
</head>
<body>
  <figure>
    <img src="https://example.com/image.jpg" alt="示例图片" loading="lazy">
    <figcaption>这是一张示例图片。</figcaption>
  </figure>
</body>
</html>
```


---

### 总结
在HTML中编写图片时，需要注意以下关键点：
1. 使用`<img>`标签，并正确设置`src`和`alt`属性。
2. 选择合适的图片格式和尺寸。
3. 使用`<figure>`和`<figcaption>`为图片添加说明。
4. 优化图片加载性能（如懒加载、压缩、CDN）。
5. 确保图片的无障碍访问。

遵循这些最佳实践，可以提升网页的用户体验和性能。