<template>
  <q-page>
    <q-toolbar style="border-bottom: 1px solid rgba(0,0,0,0.12);">
      <q-breadcrumbs>
        <q-breadcrumbs-el icon="home" :to="{ name: 'home' }" />
        <q-breadcrumbs-el label="Dashboard" icon="dashboard" to="/" />
      </q-breadcrumbs>
      <q-space />
      <q-select
        outlined
        color="light-blue-6"
        bg-color="light-blue-6"
        v-model="rangePreset"
        width="250"
        :options="rangePresets"
        size="sm"
        class="q-ml-md text-white"
      />
    </q-toolbar>
    <div class="row flex q-gutter-md q-pa-lg">
      <template v-for="(visualization, index) in visualizations">
        <q-card class="col-12 q-ma-sm" :key="index">
          <apexchart
            ref="visualization.id"
            type="area"
            :height="280"
            :options="visualization.options"
            :series="visualizationData[index]"
          ></apexchart>
        </q-card>
      </template>
    </div>
  </q-page>
</template>

<script>
import Repository from "../../repository";
import { date } from "quasar";
export default {
    data() {
        console.log(Date.now());
        return {
            rangePresets: [
                "Past 15m",
                "Past 30m",
                "Past 1h",
                "Past 6h",
                "Past 12h",
                "Past 24h",
                "Past 2d",
                "Past 7d",
                "Past 30d"
            ],
            rangePreset: "Past 1h",
            visualizations: [],
            visualizationData: [],
            refreshInterval: null
        };
    },
    beforeMount() {
        console.log("eforemount");
        this.$http
            .get("machines/5d4dae9c8b87bc00011bad31/visualizations")
            .then(response => {
                this.visualizations = response.data.map(visualization => {
                    visualization.options = JSON.parse(visualization.options);
                    return visualization;
                });
                this.visualizations.forEach(visualization => {
                    this.visualizationData.push([
                        { name: "Oil Temperature", data: [] }
                    ]);
                });
                console.log(this.visualizations);
                this.refreshDashboard();
            })
            .catch(err => {
                console.log(err);
            });
        this.refreshInterval = setInterval(() => this.refreshDashboard(), 5000);
    },
    methods: {
        refreshDashboard() {
            const fromDate = date.subtractFromDate(Date.now(), { minutes: 2 });
            this.visualizations.forEach((visualization, index) => {
                this.$http
                    .get("query", {
                        params: {
                            visualizationId: visualization.id,
                            fromTime: date.formatDate(
                                fromDate,
                                "YYYY-MM-DDTHH:mm:ss.SSSZ"
                            ),
                            toTime: date.formatDate(
                                Date.now(),
                                "YYYY-MM-DDTHH:mm:ss.SSSZ"
                            ),
                            interval: "1s"
                        }
                    })
                    .then(response => {
                        console.log(response);
                        console.log(this.$refs);
                        let lastElement = 0;
                        do {
                            lastElement = response.data.results[0].series[0].values.pop();
                        } while (lastElement[1] == 0);
                        this.$refs["visualization.id"][0].updateSeries([
                            {
                                data: response.data.results[0].series[0].values
                            }
                        ]);
                        console.log(this.visualizationData);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            });
        }
    }
};
</script>

<style lang="stylus" scoped>
.q-select {
  min-height: 28px;
}

.q-select >>> .q-field__control {
  min-height: 28px;
}

.q-select >>> .q-field__native {
  min-height: 28px;
}

.q-select >>> .q-field__append {
  min-height: 28px;
  height: 28px;
}</style>

