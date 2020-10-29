<template>
    <div id="login-form">
        <form action="#" @submit.prevent="login">
            <p>
                <input type="text" name="username" id="username" v-model="username">
            </p>
            <p>
                <input type="password" name="password" id="password" v-model="password">
            </p>
            <button type="submit">Login</button>
        </form>
    </div>
</template>
<script>
export default {
    name: 'login',
    data() {
        return{
            username: '',
            password: '',
        }
    },
    methods: {
        login(){
            this.$store.dispatch('getToken', {
                username: this.username,
                password: this.password
            })
            .then(res => {
                this.$router.push({name: "Home"})
            })
        }
    }
}
</script>