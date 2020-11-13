<template>
    <form action="#" @submit.prevent="uploadDocument">
    <input type="file" ref="file" id="file" v-on:change="onFileChange">
    <input type="text" name="desc" id="desc" v-model="desc">
    <button type="submit">Upload</button>
    </form>
</template>

<script>
export default {
    data(){
        return {
            file: null,
            desc: ''
        }
    },

    methods: {
        onFileChange(e){
            this.file = this.$refs.file.files[0];
        },

        uploadDocument(){
            this.$store.dispatch('uploadDocument', {
                doc: this.file,
                desc: this.desc
            })
            .then(res => this.$router.push({name: 'Documents'}))
            this.file = null;
            this.desc = '';
        }
    }
}
</script>