unit utypes;

{$mode objfpc}{$H+}

interface

uses
  Classes, SysUtils, forms;

type
  TCommands = (opImportMachineCmd, opExportMachineCmd, opGetMachineListCmd, opGetRunMachineListCmd,opStartMachineCmd, opStopMachineCmd, opGetInfoMachineCmd, opDeleteMachineCmd, opInsertSSHRuleNet, opInsertTomcatRuleNet, opInsertDeployServiceRuleNet,opInsertTomcatDebugRuleNet, opGetVersionVirtualBox, {opInstallVirtualBox,} opSaveStateMachineCmd, opDeleteSSHRuleNet,opDeleteTomcatRuleNet,opDeleteDeployServiceRuleNet,opDeleteTomcatDebugRuleNet,opInsertMysqlRuleNet,opDeleteMysqlRuleNet, opRefreshMainListCmd, opDownloadUpdatesCmd, opCreateSpaceCmd, opDestroySpaceCmd, opCleanUpServersCmd, opUpdateServersCmd, opRefreshSpacesListCmd, opConfigureMachineCmd, opConfigureAllMachinesRunningCmd, opRestartServersCmd, opRestartServersDebugCmd, opInsertLoggingHubRuleNet, opDeleteLoggingHubRuleNet, opRenewCertificateCmd);
  TCommandInfo = record
    Command: TCommands;
    Name: String;
    Federation:String;
    FileName: string;
    FileNameMD5: string;
    Port: string;
    Domain: string;
    Url: string;
    UrlMD5: string;
    Title: string;
    Content: string;
    SubContent: string;
    DeployServicePort: string;
    TomcatPort: string;
    ShowModalWait: boolean;
    Time: string;
    Days: integer;
  end;

  TMainRefreshInfo = record
    MachineList, MachineListRunning, MachineListPaused: string;
  end;

  TMainUpdateFormInfo = record
    Active: boolean;
  end;

  TServerRefreshInfo = record
    SpacesList: string;
    UsersList: string;
    DeployServiceVersion: string;
    MetaModelVersion: string;
  end;

  TRemoteServer = record
    Id: string;
    Host: string;
    Port: string;
    DeployType: string;
    DeployServicePort: string;
    DeployServiceDomain: string;
    DeployServiceUser: string;
    DeployServicePassword: string;
    LoggingHost: string;
    LoggingPort: string;
    UpdateAppsFullUrl: string;
    FileConfig: string;
  end;

  TRemoteServers = record
    List: TStringList;
    Locations: TStringList;
  end;

implementation

end.

