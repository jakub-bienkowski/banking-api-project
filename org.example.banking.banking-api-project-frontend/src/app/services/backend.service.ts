import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Customer} from "../models/customer";
import {environment} from "../../environments/environment";
import {CreateAccountRequest} from "../models/create-account-request";
import {Account} from "../models/account";

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http: HttpClient) {}

  public getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(environment.basePath + environment.api.customers);
  }

  public getCustomer(id: string): Observable<Customer> {
    return this.http.get<Customer>(environment.basePath + environment.api.customers + `${id}`);
  }

  public createAccount(request: CreateAccountRequest): Observable<Account> {
    return this.http.post<Account>(environment.basePath + environment.api.accounts.add, request);
  }

}
