export default class Repository {
    url = "";
    httpClient = undefined;

    constructor(url, httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    getData(filters, page, size, sort) {
        return this.httpClient.get(this.url, { params: { filters: filters, page: page, size: size, sort: sort } });
    }

    save(document) {
        return this.httpClient.post(this.url, document);
    }
}