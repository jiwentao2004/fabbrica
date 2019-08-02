<template>
  <q-card style="min-width: 280px; max-width: 550px; width:100%;">
    <q-card-section class="bg-light-blue-6 text-white">
      <div class="text-h4 text-center">Fabbrica</div>
    </q-card-section>

    <q-separator />
    <q-form @submit="onSubmit" class="q-gutter-md">
      <q-card-section>
        <q-input outlined v-model="username" label="Username" class="q-mb-sm" />
        <q-input outlined v-model="password" label="Password" type="password" />
      </q-card-section>

      <q-card-actions align="right" class="q-ma-none q-pt-none q-pr-md q-pb-md">
        <q-btn unelevated color="light-blue-6" type="submit">Login</q-btn>
      </q-card-actions>
    </q-form>
  </q-card>
</template>

<script>
import Vue from "vue";
import queryString from "query-string";
export default {
    data() {
        return {
            username: "",
            password: ""
        };
    },
    methods: {
        onSubmit() {
            const user = {
                username: this.username,
                password: this.password,
                grant_type: "password"
            };
            this.$http
                .post(
                    "http://127.0.0.1:8080/oauth/token",
                    queryString.stringify(user),
                    {
                        auth: {
                            username: "fabbrica",
                            password: "fabbrica"
                        },
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        }
                    }
                )
                .then(response => {
                    if (response.data.access_token) {
                        const accessToken = response.data.access_token;
                        Vue.prototype.$http.defaults.headers.common.Authorization =
                            "Bearer " + accessToken;
                        const refreshToken = response.data.refresh_token;
                        const loggedUser = {
                            id: response.data.id,
                            username: response.data.username,
                            name: response.data.name,
                            roles: response.data.roles
                        };
                        console.log("sfdsf");
                        this.$store
                            .dispatch("login", {
                                loggedUser,
                                accessToken,
                                refreshToken
                            })
                            .then(() => {
                                console.log("dispatch then");
                                this.$router.replace("/");
                            })
                            .catch(err => console.log(err));
                    } else {
                        throw new Error("Access Token not found.");
                    }
                })
                .catch(err => {
                    console.log(err);
                });
        }
    }
};
</script>
