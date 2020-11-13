<template>
  <div class="rating">
    <ul class="list">
      <li
        @click="rate(star)"
        v-for="star in maxStars"
        :class="{ active: star <= stars }"
        :key="star.stars"
        class="star"
      >
        <i :class="star <= stars ? 'fas fa-star' : 'far fa-star'"></i>
      </li>
    </ul>
    <button v-on:click="postRating">Rate</button>
  </div>
</template>
<script>
export default {
  name: "Rating",
  props: ["grade", "maxStars", "document"],
  data() {
    return {
      stars: this.grade,
    };
  },
  methods: {
    rate(star) {
      if (typeof star === "number" && star <= this.maxStars && star >= 0) {
        this.stars = this.stars === star ? star - 1 : star;
      }
    },
    postRating() {
      this.$store.dispatch('rateDocument', {
        document: this.document,
        rating: this.stars
      })
    }
  },
};
</script>

<style scoped>
.rating {
  width: 40%;
  margin: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  color: #b7b7b7;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 6px 33px rgba(19, 18, 18, 0.09);
  margin-bottom: 10px;
}

.star {
  font-size: 40px;
}

.star.active {
  color: gold;
}

ul {
  padding-inline-start: 0px;
}

ul li {
  display: inline-block;
}

ul li:before {
  content: "\2605";
}
</style>