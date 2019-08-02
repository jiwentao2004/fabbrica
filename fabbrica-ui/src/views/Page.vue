<template>
    <q-layout view="hHh lpR lff">

    <q-header bordered class="bg-light-blue-6">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="left = !left" />

        <q-toolbar-title>
          Fabbrica
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="left" side="left" bordered :width="200">
      <q-scroll-area class="fit">
          <q-list>

            <q-item clickable active v-ripple>
              <q-item-section avatar>
                <q-icon name="location_city" />
              </q-item-section>
              <q-item-section>
                Tenants
              </q-item-section>
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
    data () {
    return {
      left: true
    }
  }
}
</script>
