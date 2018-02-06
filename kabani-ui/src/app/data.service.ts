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
  serviceurl = 'http://localhost:8080/';
  //serviceurl= 'http://kabanip-dev.us-east-1.elasticbeanstalk.com/';

  branch = "";


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
