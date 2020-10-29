<template>
  <div id="popup">
      <div class="inner">
        <div class="header">Error</div>
        <div class="body">{{message}}</div>
        <button @click="closeFunction" class="close">Close</button>
      </div>
  </div>
</template>
<script>
export default {
    computed: {
      message(){
          return this.$store.getters.getErrorMessage
      }  
    },
    methods : {
        closeFunction(){    
            this.$store.commit('setErrorVisible')
        }
    }
}
</script>

<style>
html,body{
    padding: 0;
    margin:0;
}
#popup{
    align-items: center;
    position: absolute;
    left: 35%;
    width: 100%;
    height :100%;
    top: 20%;
}
#popup .inner{
    margin: auto;
    border-radius: 10px;
    background-color: #cfd8dc;
    position: inherit;
    height: 150px;
    width: 350px;
    text-align: center;
    font-weight: bold;
    font-size: 20px;
}

#popup .inner .header{
    font-size: 25px;
    background-color: #9ea7aa;
    border-radius: 10px 10px 0px 0px;
}
#popup .inner .close{
    margin-top: 15%;
}
</style>