<template>
    <div class="documents">
        <h1>Size <span v-text="size"></span></h1>
        <div class="document" v-for="document in documentsList" :key="document.id">
            <h2>{{document.name}}</h2>
            <p>{{document.description}}</p>
            <router-link  :to="{ name: 'DocumentView', params: {id: document.id} }">Open</router-link>
        </div>
    </div>
</template>

<script>
export default {
    computed: {
        documentsList() {
            return this.$store.getters.getsearchResults
        },
        size(){
            return this.documentsList.length
        }
    },
}
</script>

<style scoped>
.document {
    padding: 15px;
    width: 90%;
    margin: auto;
    border: 1px solid grey;
    border-radius: 5px;
    margin: 20px;
    box-shadow: 1px 3px 3px rgba(0,0,0,0.3); /* Параметры тени */
}

.document a {
    font-weight: bold;
    color: #42b983;
    text-decoration: none;
}
</style>