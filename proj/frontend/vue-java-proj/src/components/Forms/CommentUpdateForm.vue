<template>
    <form action="#" @submit.prevent="updateComment">
        <p>
            <input type="text" name="text" id="text" v-model="text">
        </p>
        <button type="submit">Ok</button>
    </form>
</template>

<script>
export default {
    props: ['comment', 'document'],
    data() {
        return {
            text: ''
        }
    },
    methods: {
        updateComment(){
            this.$store.dispatch('updateComment', {
                docId: this.document,
                commId: this.comment,
                text: this.text
            })
        }
    }
}
</script>