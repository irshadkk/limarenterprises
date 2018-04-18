import { Injectable } from "@angular/core";
import { Http, Response, Headers, RequestOptions } from "@angular/http";
import { Router } from '@angular/router';
import { Observable } from "rxjs/Rx";
import 'rxjs/add/operator/map'

@Injectable()
export class DataService {

  constructor(
    private http: Http, private router: Router
  ) { }
  cars = [
    'Ford', 'Chevrolet', 'Buick'
  ];
  // serviceurl = 'http://localhost:8888/';
  serviceurl = 'http://kabani-env.us-east-1.elasticbeanstalk.com/';

  branch = "";

  public monthSelectArr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  public yearSelectArr = ["2017", "2018", "2019", "2020", "2020", "2021",
    "2022", "2023", "2024", "2025", "2026", "2027"];

  getSelectedMonth() {
    return this.monthSelectArr[new Date().getMonth()];
  }

  getSelectedYear() {
    return new Date().getFullYear();
  }

  getUser(name, password) {
    return this.http.get(this.serviceurl + 'login/?name=' + name + '&password=' + password)
      .map((res: Response) => res.json());
  }
  getData(url) {
    return this.http.get(url)
      .map((res: Response) => res.json());
  }
  getPostData(url, bodyParam) {
    return this.http.post(url, bodyParam)
      .map((res: Response) => res.json());
  }

  getBranch() {
    if (localStorage.getItem("branch") == null || localStorage.getItem("branch") == undefined) {
      this.logout()
    } else {
      localStorage.getItem("branch")
    }
  }
  setBranch(branch) {
    localStorage.setItem("branch", branch);
  }
  appDefined() {
    if (localStorage.getItem("branch") == null || localStorage.getItem("branch") == undefined) {
      this.logout()
    } else {
      return true;
    }
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
