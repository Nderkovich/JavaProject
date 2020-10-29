<template>
    <div>
        <h1>{{document.name}}</h1>
        <p>{{document.description}}</p>
        <h3>Rating: {{document.rating}}</h3>
        <Rating :grade="1" :maxStars="5" :document="document.id"/>
        <div class="controls">
            <button v-on:click="download" class="download">Download</button>
            <button v-if="isAdmin" v-on:click="deleteDocument" class="delete">Delete</button>
        </div>
        <Comment v-for="comment in commentsList" :key="comment.id" :comment="comment" :document="document.id"/>
        <CommentForm :id="document.id"/>
    </div>
</template>

<script>
import Comment from "./Comment";
import CommentForm from "./Forms/CommentForm";
import Rating from './Stars'

export default {

    components: {
        Rating,
        Comment,
        CommentForm
    },
    computed: {
        document() {
            return this.$store.getters.getDocument
        },
        commentsList() {
            return this.$store.getters.getComments
        },
        rating() {
            return this.$store.getters.getRating
        },
        isAdmin() {
          return this.$store.getters.isAdmin
        }
    },
    beforeCreate() {
        this.$store.dispatch('getDocument', this.$route.params.id);
    },
    methods: {
        download() {
            this.$store.dispatch('downloadDocument', this.$route.params.id)
        },
        deleteDocument() {
            this.$store.dispatch('deleteDocument', this.$route.params.id)
            .then(this.$router.push({name: "Documents"}).catch(err => {}))
        }
    }
}
</script>

<style scoped>
  .download {
      margin-bottom: 10px;
  }
  .delete {
      background-color: red;
  }
  .controls {
      display: flex;
      flex-direction: column;
      width: 100px;
      margin: auto;
  }
</style>

