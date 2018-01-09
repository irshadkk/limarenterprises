import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpResponse, HttpEventType } from '@angular/common/http';
import { UploadFileService } from '../upload-file.service';

@Component({
  selector: 'list-upload',
  templateUrl: './list-upload.component.html',
  styleUrls: ['./list-upload.component.css']
})
export class ListUploadComponent implements OnInit {

  selectedFiles: FileList
  currentFileUpload: File
  
  progressArr=[];

  constructor(private uploadService: UploadFileService) { }

  ngOnInit() {
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }
  
  upload() {
  //let url='http://localhost:9000/demo/upload/'+this.dataService.getBranch();
  let url='http://kabanip-dev.us-east-1.elasticbeanstalk.com/upload/branch1'; 
    for (var i=0;i<this.selectedFiles.length;i++){
    let progress = { percentage: 0 }
    console.log("000"+JSON.stringify(this.selectedFiles))
    this.currentFileUpload = this.selectedFiles.item(i)
    this.uploadService.pushFileToStorage(this.currentFileUpload,url).subscribe(event => {
      if (event.type === HttpEventType.UploadProgress) {
        progress.percentage = Math.round(100 * event.loaded / event.total);
        this.progressArr.push(progress)
      } else if (event instanceof HttpResponse) {
        alert('File is completely uploaded!');
      }
    })
    } 
    

    this.selectedFiles = undefined
  }
}
