<template>
    <div id="login-form">
        <h1>Register</h1>
        <form action="#" @submit.prevent="login">
            <p>
                <input type="text" name="username" id="username" v-model="username">
            </p>
            <p>
                <input type="password" name="password" id="password" v-model="password">
            </p>
            <button type="submit">Register</button>
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
            this.$store.dispatch('register', {
                username: this.username,
                password: this.password
            })
            .then(res => {
                this.$router.push({name: "Login"})
            })
        }
    }
}
</script>