# dropwizard-config-encrypt

This project will decrypt values in your DW config using KMS.

## Usage

Import the library into your DW project. Add the decryption bundle or command (only one of both is required) by overriding Application#initialize.
In both cases a function is required that will get the encryption client from the config.

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