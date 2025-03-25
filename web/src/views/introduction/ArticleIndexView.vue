<template>
  <ContentField>
      <ArticleView :article-id="articleId" />
  </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import ArticleView from "@/components/ArticleView.vue";

export default{
  computed: {
    articleId() {
        return parseInt(this.$route.params.articleId);
    }
  },
  components :{
    ContentField,
    ArticleView,
  },
}
</script>
 
<style scoped>

</style>
