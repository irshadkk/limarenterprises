import { Component } from '@angular/core';
import { Http, Response } from '@angular/http';
import {RequestOptions, Request, RequestMethod} from '@angular/http';
import 'rxjs/add/operator/map'
import {Observable} from 'rxjs/Rx';


import { DataService } from '../../data.service';

@Component({
  templateUrl: 'uploadcsv.component.html'
})
export class UploadCsvComponent {
  public testarr;
  constructor(private http: Http,private dataService: DataService) {

    }

    title = 'file uploader';
  fileChange(event) {
    let fileList: FileList = event.target.files;
    if(fileList.length > 0) {
        let file: File = fileList[0];
        let formData:FormData = new FormData();
        formData.append('file', file, file.name);
        let headers = new Headers();
        /** No need to include Content-Type in Angular 4 */
        let url='http://kabanip-dev.us-east-1.elasticbeanstalk.com/upload/'+this.dataService.getBranch();
        this.http.post(url, formData)
            .map(res => this.testarr=res.json())
            .catch(error => Observable.throw(error))
            .subscribe(
                data => console.log('success'),
                error => console.log(error)
            )
    }
}

}
