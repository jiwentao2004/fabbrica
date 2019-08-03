<template>
  <q-layout view="hHh lpR lff">
    <q-header bordered class="bg-light-blue-6">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="left = !left" />

        <q-toolbar-title>Fabbrica</q-toolbar-title>
        <q-space />
        <q-chip icon-right="expand_more" v-ripple id="usermenu">
          <q-avatar
            icon="person"
            class="bg-white"
            text-color="light-blue-6"
            font-size="25px"
            size="32px"
          />
          {{ username }}
          <q-menu
            transition-show="jump-down"
            transition-hide="jump-up"
            anchor="bottom right"
            self="top right"
          >
            <q-list style="min-width: 100px">
              <q-item clickable v-close-popup @click="logout">
                <q-item-section>Logout</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-chip>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="left" side="left" bordered :width="200">
      <q-scroll-area class="fit">
        <q-list>
          <q-item clickable active v-ripple :to="{ name: 'machines' }">
            <q-item-section avatar>
              <q-icon name="fas fa-cog" />
            </q-item-section>
            <q-item-section>Machines</q-item-section>
          </q-item>
          <q-item clickable active v-ripple :to="{ name: 'machine-models' }">
            <q-item-section avatar>
              <q-icon name="fas fa-cogs" />
            </q-item-section>
            <q-item-section>Machine Models</q-item-section>
          </q-item>
          <q-item clickable active v-ripple :to="{ name: 'factories' }">
            <q-item-section avatar>
              <q-icon name="domain" />
            </q-item-section>
            <q-item-section>Factories</q-item-section>
          </q-item>
          <q-item clickable active v-ripple :to="{ name: 'tenants' }">
            <q-item-section avatar>
              <q-icon name="location_city" />
            </q-item-section>
            <q-item-section>Tenants</q-item-section>
          </q-item>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
export default {
    data() {
        return {
            left: true,
            username: this.$store.state.user.name
        };
    },
    methods: {
        logout() {
            this.$store
                .dispatch("logout")
                .then(() => this.$router.replace({ name: "login" }))
                .catch(err => console.log(err));
        }
    }
};
</script>

