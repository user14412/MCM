<template>
    <ContentField>
        <ExhibitView :exhibitId="exhibitId" />
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import ExhibitView from "@/components/ExhibitView.vue";

export default {
    computed: {
        exhibitId() {
            return parseInt(this.$route.params.exhibitId);
        }
    },
    components: {
        ContentField,
        ExhibitView,
    },
}
</script>

<style scoped></style>
