<template>
    <form action="#" @submit.prevent="postComment">
        <p>
            <input type="text" name="text" id="text" v-model="text">
        </p>
        <button type="submit">Post comment</button>
    </form>
</template>

<script>
export default {

    props: ['id'],
    data() {
        return{
            text: '',
        }
    },
    methods: {
        postComment(){
            this.$store.dispatch('addComment', {
                document: this.id,
                text: this.text
            })
            this.text = ''
        }
    }
}
</script>