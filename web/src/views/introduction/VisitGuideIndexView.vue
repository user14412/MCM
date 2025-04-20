<template>
  <ContentField>
      <ArticleView :articleId="articleId"/>
  </ContentField>
</template>

<script>
// 视图：参观须知
import ContentField from "@/components/ContentField.vue"
import ArticleView from "@/components/ArticleView.vue";

export default{
  components :{
    ContentField,
    ArticleView,
  },
  setup() {
    // const articleId = 4;
    const articleId = 27;
    return {
      articleId,
    }
  },
}
</script>
 
<style scoped>

</style>
