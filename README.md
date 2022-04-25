
# Technical Task Elasticsearch
## Project Files
Here is the breakdown of the project directory:
* **Kibana Dashboard:** Contains exported Kibana Dashboard and screenshots
* **Output:** Preconfigured Storage Folder
* **Uploads:** Preconfigured Input Folder
* **src:** project source files
	* **Controllers:** Contains the UploadController with the exposed upload API
	* **Elasticsearch:** Contains index json files and a class to handle document indexing
	* **Models:** Contains classes to represent the Archive and File objects. Also contains Upload API response class.
	* **ApplicationConfig:** Configuration Class
	* **FileProcessor:** class used for the processing of archives
	* **MyFileChangeListener:** Listener for changes on the Input folder
	* **StorageService:** Service class for handling the storage of files passed in the API

## Screenshots

### File Processing:
Output:
![Output](Screenshots/Output.PNG)

Storage Folder:

![Storage Folder](Screenshots/StorageFolder.PNG)

Processed Folder:

![Processed Archives](Screenshots/processed.PNG)

Extracted Doc Files:

![Extracted doc Files](Screenshots/Doc.PNG)

### Kibana Dashboard:
Archives Statistics:
![Archives Statistics](Kibana%20Dashboard/Archives.PNG)

Files Statistics:
![Files Statistics](Kibana%20Dashboard/FilesPNG.PNG)
