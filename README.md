# dropwizard-config-encrypt

## What is this plugin?
This Dropwizard plugin will decrypt values in your DW config using KMS. In the future other ways to decrypt and encrypt values may be added.

When your Dropwizard app starts with this plugin configured, it will look through the configuration to find fields embedded in ENC[encText] 
and attempt to decrypt those values. After decrypting the values will be replaced with the decrypted values in memory. 
Do not use this plugin if it's a problem to have readable passwords in memory. In most cases it's enough when plain text 
passwords are not pushed to VCS.

## Usage
Import the library into your DW project. Add the decryption bundle or command (only one of both is required) by overriding Application#initialize.
In both cases a function is required that will get the encryption client from the config.

You may also extend our custom Application as described below.

### Configuration
The KMS client needs to know where your KMS key is located and which key it is.
```
encryptionClient:
  type: kms
  regions: EU_WEST_1
  key: "arn:aws:kms:eu-west-1:000000000:key/0000000-0000-0000-0000-0000000000"
```

Add the following to your Configuration:
```
@NotNull
@JsonProperty
private EncryptionClientFactory encryptionClient;
```

In test/local configs you can run without encryption without changing any code by using the following config:
```
encryptionClient:
  type: none
```

### Bundle
Using bundle integration is the recommended way of integrating this plugin into your project. 
It's the least invasive, doesn't require any other changes to your application.

```
@Override
public void initialize(Bootstrap<ProjectConfig> bootstrap) {
    bootstrap.addBundle(new DecryptionConfiguredBundle<>(ProjectConfig::getEncryptionClient));
}
```

### Command

```
@Override
public void initialize(Bootstrap<ProjectConfig> bootstrap) {
    // add a startup command that enables decrypting the config
    bootstrap.addCommand(new ConfigDecryptingServerCommand(this, ProjectConfig::getEncryptionClient));
}
```

Now start your project with the parameter "encserver" instead of "server".

### Extend ConfigDecryptingApplication
Instead of extending io.dropwizard.Application, extend com.tn.dw.integration.ConfigDecryptingApplication.
Your project's Configuration should also extend com.tn.dw.integration.ConfigDecryptingConfiguration.

In most cases it's not recommended to use this integration as its quite invasive to change your class hierarchy.
