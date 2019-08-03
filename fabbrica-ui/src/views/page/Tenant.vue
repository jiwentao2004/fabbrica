<template>
  <q-page>
    <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);">
      <q-breadcrumbs>
        <q-breadcrumbs-el icon="home" :to="{ name: 'home' }" />
        <q-breadcrumbs-el label="Tenants" icon="location_city" to="/tenants" />
      </q-breadcrumbs>
      <q-space />
      <q-btn unelevated color="light-blue-6" :to="{ name: 'newTenant' }">New</q-btn>
    </q-toolbar>
    <div class="row q-pa-md flex">
      <filter-list class="full-width full-height" :repository="repository" :columns="columns" />
    </div>
  </q-page>
</template>

<script>
import FilterList from "../../components/FilterList.vue";
import Repository from "../../repository";
export default {
    data() {
        return {
            buttonsize: "sm",
            repository: new Repository("tenants", this.$http),
            columns: [
                { name: "code", label: "Tenant Code", sortable: true },
                { name: "name", label: "Tenant Name", sortable: true }
            ]
        };
    },
    components: {
        "filter-list": FilterList
    }
};
</script>

