# amazons3automirrorApplication

A utility for mirroring content from local disk PC to Amazon S3 bucket by manulSYNC/AutoSYNC

Designed to be lightning-fast and highly concurrent, with modest CPU and memory requirements.

Note: The object with same name exist in the destination bucket will be replaced while mirroring (replaced with newer one)

Motivation

I started this project because in my office completing of works and moving it to S3-bucket from local disk had a separate process and time consuming so i thought of creating a application that automatically create a mirror-file of your work in the bucket u specify and do auto replacement of file with updated file i.e The object with same name exist in the destination bucket will be replaced while mirroring (replaced with newer one)

AWS Credentials

The application will first look for credentials in your system environment. If variables named ACCESS KEY and SECRET KEY are defined, then you need to specify the local disk directory and select the bucket which will be displayed in list box based on your amazon s3 account

manulSYNC do the movement of data from local disk to your bucket specified when clicked or-else autoSYNC do auto mirroring of data for every time specified

System Requirements

1.Java 6 or later version 2.AWS SDK for java or it need AWS toolkit for eclipse because i created using eclipse tool you may use that too

