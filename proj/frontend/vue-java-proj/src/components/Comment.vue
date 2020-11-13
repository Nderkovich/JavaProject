<template>
    <div class="comment">
        <p>{{comment.text}}</p>
        <button v-if="canChange" @click='visible = !visible'>Update</button>
        <button v-if="canChange" @click='deleteComment'>Delete</button>
        <CommentUpdateForm v-if="visible" :document="document" :comment="comment.id"/>
    </div>
</template>

<script>
import CommentUpdateForm from "./Forms/CommentUpdateForm"

export default {
    props: ['comment', 'document'],
    components: {
        CommentUpdateForm
    },
    computed: {
        canChange(){
            return (this.$store.getters.getUserId == this.comment.userId);
        }
    },
    data() {
        return {
            visible: false
        }
    },
    methods: {
        deleteComment(){
            console.log(this.comment.id)
            this.$store.dispatch('deleteComment', {doc: this.document,comm: this.comment.id})
        }
    }
}
</script>

<style scoped>
    .comment {
        text-align: start;
        margin: 0 20% 10px 20%;
        padding: 10px;
        box-shadow: 0px 0px 3px rgba(0,0,0,0.3);
        border-radius: 10px;
    }
</style>