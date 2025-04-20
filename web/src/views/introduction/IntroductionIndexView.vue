<template>
  <ContentField>
      <ArticleView :articleId="articleId"/>
  </ContentField>
</template>

<script>
// 视图：本馆概况
import ContentField from "@/components/ContentField.vue"
import ArticleView from "@/components/ArticleView.vue";

export default{
  components :{
    ContentField,
    ArticleView,
  },
  setup() {
    const articleId = 26;
    return {
      articleId,
    }
  },
}
</script>
 
<style scoped>

</style>
