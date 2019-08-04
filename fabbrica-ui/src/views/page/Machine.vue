<template>
  <q-page>
    <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);">
      <q-breadcrumbs>
        <q-breadcrumbs-el icon="home" :to="{ name: 'home' }" />
        <q-breadcrumbs-el label="Machines" icon="domain" :to="{ name: 'machines' }" />
      </q-breadcrumbs>
      <q-space />
      <q-btn
        unelevated
        color="light-blue-6"
        :to="{ name: 'machine-form', params: { id: 'new' } }"
      >New</q-btn>
    </q-toolbar>
    <div class="row q-pa-md flex">
      <filter-list
        class="full-width full-height"
        :repository="repository"
        :columns="columns"
        :rowClick="rowClick"
      />
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
            repository: new Repository("machines", this.$http),
            columns: [
                { name: "code", label: "Machine Code", sortable: true },
                { name: "name", label: "Machine Name", sortable: true },
                {
                    name: "machineModel.name",
                    label: "Machine Model",
                    sortable: true
                },
                { name: "factory.name", label: "Factory", sortable: true },
                { name: "factory.tenant.name", label: "Tenant", sortable: true }
            ]
        };
    },
    components: {
        "filter-list": FilterList
    },
    methods: {
        rowClick(id) {
            this.$router.push({
                name: "machine-form",
                params: { id: id }
            });
        }
    }
};
</script>

