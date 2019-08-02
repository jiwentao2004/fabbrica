<template>
  <q-layout view="lhr lpr lfr">
    <q-page-container>
      <q-page class="flex justify-center q-pa-lg">
        <div class="row flex">
          <div class="col-auto">
            <router-view />
          </div>
        </div>
      </q-page>
    </q-page-container>
  </q-layout>
</template>

<script>
export default {
    data() {
        return {
            left: true
        };
    }
};
</script>

