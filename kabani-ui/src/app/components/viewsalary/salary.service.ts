import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { DataService } from '../../data.service';

import 'rxjs/add/operator/toPromise';
import { Observable } from "rxjs/Observable";
import "rxjs/Rx";

@Injectable()
export class SalaryService {
  constructor(private http: Http, private dataService: DataService) { }
  private headers = new Headers({ 'Content-Type': 'application/json' });
  createHeader(): Headers {
    return new Headers({ 'Content-Type': 'application/json' });
  }
  setHeaderParam(header: Headers, key: string, value: string): Headers {
    header.append(key, value);
    return header
  }
  getSalaryStatus(year: number, month: number): Observable<number> {
    this.headers = this.createHeader();
    return this.http.get(this.dataService.serviceurl + `salary/salaryGenerated?year=${year}&month=${month + 1}`, { headers: this.headers })
      .map(response => { return response.json(); })
      .catch(error => { return this.handleError(error, 'getSalaryStatus') })
  }

  generateSalary(year: any, month: any) {
    this.headers = this.createHeader();
    return this.http.post(this.dataService.serviceurl + `salary/generateSalary/${year}/${month}`, { headers: this.headers })
      .map(response => { return response.json(); })
      .catch(error => { return this.handleError(error, 'generateSalary') })
  }

  getSalary(year: any, month: any) {
    this.headers = this.createHeader();
    return this.http.get(this.dataService.serviceurl + `salary/getSalary?year=${year}&month=${month + 1}`, { headers: this.headers })
      .map(response => { return response.json(); })
      .catch(error => { return this.handleError(error, 'getSalary') })
  }

  deletItem(item) {
    this.headers = this.createHeader();
    return this.http.post(this.dataService.serviceurl + `salary/resetAll`, { headers: this.headers })
      .map(response => { return response.json(); })
      .catch(error => { return this.handleError(error, 'resetAll') })
  }

  generateSalaryExcel(year: any, month: any) {
    // this.headers = this.createHeader();
    // return this.http.get(this.dataService.serviceurl + `salary/getSalaryExcel?year=${year}&month=${month + 1}`, { headers: this.headers })
    //   .toPromise()
    //   .then(response => {
    //     return response;
    //   })
    //   .catch(this.handleError);
    window.open(this.dataService.serviceurl + `salary/getSalaryExcel?year=${year}&month=${month}`, "_target")
  }

  loadActiveLoans(year: number, month: number): Observable<[any]> {
    this.headers = this.createHeader();
    return this.http.get(this.dataService.serviceurl + `salary/getActiveLoans?year=${year}&month=${month + 1}`, { headers: this.headers })
      .map(response => { return response.json(); })
      .catch(error => { return this.handleError(error, 'loadActiveLoans') })
  }
  loadActiveAdvances(year: number, month: number): Observable<[any]> {
    this.headers = this.createHeader();
    return this.http.get(this.dataService.serviceurl + `salary/getActiveAdvances?year=${year}&month=${month + 1}`, { headers: this.headers })
      .map(response => { return response.json(); })
      .catch(error => { return this.handleError(error, 'loadActiveAdvances') })
  }


  private handleError(error: any, method: string): Observable<any> {
    console.error('An error occurred in Salary Service @ method' + method, error);
    return Observable.throw(error.message || error);
  }
}
