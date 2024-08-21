# Forge Display Test Cross Version Notes

## 1.14 - 1.16.5
```
ModLoadingContext.get().registerExtensionPoint(
        ExtensionPoint.DISPLAYTEST,
        () ->Pair.of(
                () -> FMLNetworkConstants.IGNORESERVERONLY, (incoming, isNetwork) -> true));
```

## 1.17 - 1.17.1
```
ModLoadingContext.get().registerExtensionPoint(
        IExtensionPoint.DisplayTest.class,
        () -> new IExtensionPoint.DisplayTest(
                () -> FMLNetworkConstants.IGNORESERVERONLY, (incoming, isNetwork) -> true));
```

## 1.18 - 1.20.2
```
ModLoadingContext.get().registerExtensionPoint(
        IExtensionPoint.DisplayTest.class,
        () -> new IExtensionPoint.DisplayTest(
                () -> NetworkConstants.IGNORESERVERONLY, (incoming, isNetwork) -> true));
```

## 1.18.2 - 1.21.1
```
ModLoadingContext.get().registerExtensionPoint(
        IExtensionPoint.DisplayTest.class,
        () -> new IExtensionPoint.DisplayTest(
                () -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (incoming, isNetwork) -> true));
```
