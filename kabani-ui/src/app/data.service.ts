import {Injectable} from "@angular/core";
import {Http, Response, Headers, RequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import 'rxjs/add/operator/map'

@Injectable()
export class DataService {

  constructor (
    private http: Http
  ) {}
  cars = [
    'Ford','Chevrolet','Buick'
  ];
  branch="";


  getUser(name,password) {
    return this.http.get('http://kabanip-dev.us-east-1.elasticbeanstalk.com/login/?name='+name+'&password='+password)
    .map((res:Response) => res.json());
  }
  getData(url) {
    return this.http.get(url)
    .map((res:Response) => res.json());
  }
  getPostData(url,bodyParam) {
    return this.http.post(url,bodyParam)
    .map((res:Response) => res.json());
  }
  
  getBranch(){
    return this.branch;
  }
   setBranch(branch){
     this.branch=branch;
  }

}
