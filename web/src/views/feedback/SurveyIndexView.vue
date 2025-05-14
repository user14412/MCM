<template>
    <div>
        问卷调查
    </div>
</template>

<script>
import { useRouter } from 'vue-router';

export default {
    setup() {
        const router = useRouter();
        const surveyId = 2;

        router.push({path: `/survey/${surveyId}`});

        return {
        };
    }
}
</script>

<style scoped></style>
