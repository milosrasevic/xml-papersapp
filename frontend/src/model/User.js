import * as _ from 'lodash';

export class User {
    constructor(data) {
        _.assignWith(this, data);
    }
}