<template>
  <q-page>
    <q-form>
      <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);">
        <q-breadcrumbs>
          <q-breadcrumbs-el icon="home" :to="{ name: 'home' }" />
          <q-breadcrumbs-el
            label="Machine Models"
            icon="location_city"
            :to="{ name: 'machine-models' }"
          />
          <q-breadcrumbs-el label="New" icon="add" />
        </q-breadcrumbs>
        <q-space />
        <q-btn v-if="edit" unelevated color="light-blue-6 q-mr-sm" @click="save">Save</q-btn>
        <q-btn v-if="!edit" unelevated color="light-blue-6 q-mr-sm" @click="enableEdit">Edit</q-btn>
        <q-btn v-if="edit" unelevated color="blue-grey-6" @click="cancel">Cancel</q-btn>
      </q-toolbar>
      <div class="row q-pa-md flex">
        <q-card class="full-width q-pa-md">
          <div class="row q-gutter-md">
            <div class="col-md-4 col-sm-6">
              <q-input
                outlined
                v-model="data.code"
                label="Machine Model Code"
                class="q-mb-sm"
                :disable="!edit"
              />
              <q-input
                outlined
                v-model="data.name"
                label="Machine Model Name"
                class="q-mb-sm"
                :disable="!edit"
              />
            </div>
            <div class="col-md-4 col-sm-6">
              <q-input
                outlined
                v-model="measurement"
                label="Measurement"
                class="q-mb-sm"
                v-show="edit"
                :disable="!edit"
              >
                <template v-slot:append>
                  <q-btn flat outlined icon="add" @click="addMeasurement" class="cursor-pointer" />
                </template>
              </q-input>
              <q-slider
                v-model="frequency"
                :label-value="frequency + ' ms'"
                :min="0"
                :max="10000"
                :step="10"
                snap
                label
                label-always
                v-show="edit"
                color="light-blue-6"
              />
              <q-list
                bordered
                separator
                v-if="data.measurements && data.measurements.length > 0"
                class="rounded-borders q-mb-sm"
                v-model="rerender"
              >
                <q-item
                  v-for="(measurement, index) in data.measurements"
                  :key="index"
                  clickable
                  v-ripple
                >
                  <q-item-section avatar>
                    <q-avatar color="primary" text-color="white">{{ measurement.name.charAt(0) }}</q-avatar>
                  </q-item-section>

                  <q-item-section>
                    <q-item-label>{{ measurement.name }} : {{ measurement.frequency }} ms</q-item-label>
                    <q-item-label
                      caption
                      lines="1"
                      v-for="(field, fIndex) in measurement.fields"
                      :key="fIndex"
                    >
                      {{ field.name }} : {{ field.type }}
                      <q-btn
                        flat
                        outlined
                        icon="close"
                        color="red-6"
                        @click="removeField(index, fIndex)"
                        :disable="!edit"
                        v-show="edit"
                        style="padding: 0px; !important;"
                        size="xs"
                      />
                    </q-item-label>
                  </q-item-section>

                  <q-item-section side>
                    <q-btn
                      flat
                      outlined
                      icon="add"
                      color="light-blue-6"
                      @click="addField(index)"
                      :disable="!edit"
                      v-show="edit"
                    />
                  </q-item-section>
                  <q-item-section side style="padding-left: 0px !important;">
                    <q-btn
                      flat
                      outlined
                      icon="close"
                      color="red-6"
                      @click="removeMeasurement(index)"
                      :disable="!edit"
                      v-show="edit"
                    />
                  </q-item-section>
                </q-item>
              </q-list>
              <q-dialog v-model="showAddFieldDialog">
                <q-card style="width: 300px" class="q-px-sm q-pb-md">
                  <q-card-section>
                    <div class="text-h6">Add Field</div>
                  </q-card-section>

                  <q-input
                    outlined
                    v-model="field.name"
                    label="Field Name"
                    class="q-mb-sm"
                    :disable="!edit"
                  />
                  <q-select
                    outlined
                    v-model="field.type"
                    :options="fieldTypes"
                    label="Type"
                    class="q-mb-sm"
                  />
                  <q-btn unelevated color="light-blue-6 q-mr-sm" @click="addFieldConfirm">Add</q-btn>
                  <q-btn unelevated color="blue-grey-6" @click="addFieldCancel">Cancel</q-btn>
                </q-card>
              </q-dialog>
              <q-select
                outlined
                v-model="selectedVisualization"
                use-input
                hide-selected
                fill-input
                input-debounce="0"
                label="Visualization"
                :options="visualizationOptions"
                option-label="name"
                option-value="id"
                emit-value
                map-options
                @filter="visualizationFilter"
                @input="visualizationInput"
                :disable="!edit"
                class="q-mb-sm"
              >
                <template v-slot:option="scope">
                  <q-item v-bind="scope.itemProps" v-on="scope.itemEvents">
                    <q-item-section>
                      <q-item-label caption>{{scope.opt.code}}</q-item-label>
                      <q-item-label>{{ scope.opt.name }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </template>
                <template v-slot:no-option>
                  <q-item>
                    <q-item-section class="text-grey">No Visualizations</q-item-section>
                  </q-item>
                </template>
              </q-select>
              <q-list
                bordered
                separator
                v-if="data.visualizations && data.visualizations.length > 0"
                class="rounded-borders"
                v-model="rerender"
              >
                <q-item
                  v-for="(visualization, index) in data.visualizations"
                  :key="index"
                  clickable
                  v-ripple
                >
                  <q-item-section avatar>
                    <q-avatar color="primary" text-color="white">{{ visualization.name.charAt(0) }}</q-avatar>
                  </q-item-section>

                  <q-item-section>
                    <q-item-label>{{ visualization.name }}</q-item-label>
                    <q-item-label caption lines="1">{{ visualization.code }}</q-item-label>
                  </q-item-section>

                  <q-item-section side>
                    <q-btn
                      flat
                      outlined
                      icon="close"
                      color="red-6"
                      @click="removeVisualization(index)"
                      :disable="!edit"
                      v-show="edit"
                    />
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
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
            data: {
                name: "",
                code: ""
            },
            dataCopy: undefined,
            edit: false,
            loading: false,
            measurement: "",
            frequency: 1000,
            rerender: false,
            showAddFieldDialog: false,
            field: { name: "", type: "BOOLEAN" },
            fieldTypes: ["BOOLEAN", "FLOAT", "INTEGER", "STRING", "TIMESTAMP"],
            addFieldIndex: -1,
            selectedVisualization: {},
            visualizationOptions: [],
            repository: new Repository("machineModels", this.$http),
            visualizationRepository: new Repository(
                "visualizations",
                this.$http
            )
        };
    },
    beforeMount() {
        if (this.$route.params.id != "new") {
            this.repository
                .get(this.$route.params.id)
                .then(response => {
                    this.data = response.data;
                })
                .catch(err => {
                    console.log(err);
                });
        } else {
            this.edit = true;
        }
    },
    methods: {
        cancel() {
            if (this.$route.params.id != "new") {
                this.edit = false;
                this.data = this.dataCopy;
            } else {
                this.$router.go(-1);
            }
        },
        save() {
            this.$q.loading.show();
            this.repository
                .save(this.data)
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
        },
        enableEdit() {
            this.edit = true;
            this.dataCopy = Object.assign({}, this.data);
        },
        addMeasurement() {
            if (!this.data.measurements) {
                this.data.measurements = [];
            }
            const exist =
                this.data.measurements
                    .map(measurement => {
                        return measurement.name;
                    })
                    .indexOf(this.measurement) >= 0
                    ? true
                    : false;
            if (!exist) {
                this.data.measurements.push({
                    name: this.measurement,
                    frequency: this.frequency
                });
            }
            this.measurement = "";
            this.frequency = 1000;
        },
        removeMeasurement(index) {
            console.log("remove");
            this.data.measurements.splice(index, 1);
            this.rerender = !this.rerender;
        },
        addField(index) {
            this.showAddFieldDialog = true;
            this.addFieldIndex = index;
        },
        addFieldConfirm() {
            if (!this.data.measurements[this.addFieldIndex].fields) {
                this.data.measurements[this.addFieldIndex].fields = [];
            }
            if (this.field.name != "") {
                const exist =
                    this.data.measurements[this.addFieldIndex].fields
                        .map(field => {
                            return field.name;
                        })
                        .indexOf(this.field.name) >= 0
                        ? true
                        : false;
                if (!exist) {
                    this.data.measurements[this.addFieldIndex].fields.push(
                        this.field
                    );
                }
                this.field = { name: "", type: "BOOLEAN" };
                this.showAddFieldDialog = false;
                this.addFieldIndex = -1;
            }
        },
        addFieldCancel() {
            this.showAddFieldDialog = false;
            this.addFieldIndex = -1;
        },
        removeField(mIndex, fIndex) {
            this.data.measurements[mIndex].fields.splice(fIndex, 1);
            this.rerender = !this.rerender;
        },
        visualizationFilter(val, update, abort) {
            let filter = "";
            if (val) {
                filter = "name,like," + val + ";";
            }
            this.visualizationRepository
                .getData(filter, 0, 20, "+name")
                .then(response => {
                    update(() => {
                        this.visualizationOptions = response.data;
                    });
                })
                .catch(err => {
                    console.log(err);
                    abort();
                });
        },
        visualizationInput(value) {
            if (!this.data.visualizations) {
                this.data.visualizations = [];
            }
            const exist =
                this.data.visualizations
                    .map(visualization => {
                        return visualization.id;
                    })
                    .indexOf(value) >= 0
                    ? true
                    : false;
            if (!exist) {
                this.data.visualizations.push(
                    this.visualizationOptions.filter(visualization => {
                        return value == visualization.id;
                    })[0]
                );
            }
            this.selectedVisualization = {};
        },
        removeVisualization(index) {
            console.log("remove");
            this.data.visualizations.splice(index, 1);
            this.rerender = !this.rerender;
        }
    }
};
</script>

