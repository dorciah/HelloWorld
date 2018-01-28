import {ClientRequest} from './clientRequest';
import {Result} from './result';

export class ClientResponse {
  effectiveDate: string;
  rate: number;
  currency: string;
  homeCurrency: string;
  request: ClientRequest;
  results: Result[];
}
