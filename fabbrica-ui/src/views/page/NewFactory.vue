<template>
  <q-page>
    <q-form>
      <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);">
        <q-breadcrumbs>
          <q-breadcrumbs-el icon="home" :to="{ name: 'home' }" />
          <q-breadcrumbs-el label="Factories" icon="domain" :to="{ name: 'factories' }" />
          <q-breadcrumbs-el label="New" icon="add" :to="{ name: 'newFactory' }" />
        </q-breadcrumbs>
        <q-space />
        <q-btn unelevated color="light-blue-6 q-mr-sm" @click="save">Save</q-btn>
        <q-btn unelevated color="blue-grey-6" @click="cancel">Cancel</q-btn>
      </q-toolbar>
      <div class="row q-pa-md flex">
        <q-card class="full-width q-pa-md">
          <div class="row q-gutter-md">
            <div class="col-md-4 col-sm-6">
              <q-input outlined v-model="code" label="Factory Code" class="q-mb-sm" />
              <q-input outlined v-model="name" label="Factory Name" class="q-mb-sm" />
            </div>
            <div class="col-md-4 col-sm-6"></div>
          </div>
        </q-card>
      </div>
    </q-form>
  </q-page>
</template>

<script>
import Repository from "../../repository";
export default {
    data() {
        return {
            code: "",
            name: "",
            loading: false,
            repository: new Repository("factories", this.$http)
        };
    },
    methods: {
        cancel() {
            this.$router.go(-1);
        },
        save() {
            this.$q.loading.show();
            this.repository
                .save({ code: this.code, name: this.name })
                .then(response => {
                    if (response.status == 200) {
                        this.$q.loading.hide();
                        this.$router.go(-1);
                    }
                })
                .catch(err => {
                    this.loading = false;
                    console.log(err);
                });
        }
    }
};
</script>

