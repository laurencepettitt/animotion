package com.qusion.vos.beta

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.viro.core.*


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    protected lateinit var mViroView: ViroView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViroView = ViroViewScene(this, object : ViroViewScene.StartupListener {
            override fun onSuccess() {
                // Start building your scene here! We provide a sample "Hello World" scene
                createHelloWorldScene()
            }

            override fun onFailure(error: ViroViewScene.StartupError, errorMessage: String) {
                // Fail as you wish!
            }
        })
        setContentView(mViroView)
    }

    private fun createHelloWorldScene() {
        // Create a new Scene and get its root Node
        val scene = Scene()
        val rootNode: Node = scene.getRootNode()

        val avatar = Object3D()
        avatar.loadModel(
            mViroView.viroContext,
            Uri.parse("file:///android_asset/avatar1.gltf"),
            Object3D.Type.GLTF,
            object : AsyncObject3DListener {

                override fun onObject3DFailed(p0: String?) {
                    Log.w(TAG, "Failed to load the model");
                }

                override fun onObject3DLoaded(p0: Object3D?, p1: Object3D.Type?) {
                    Log.i(TAG, "Successfully loaded the model!");
                }
            })

        avatar.setPosition(Vector(0.0, 0.0, -3.0))

        val spotlight = Spotlight()
        spotlight.position = Vector(-1.0, 1.0, 1.0)
        spotlight.direction = Vector(1.0, 0.0, 0.0)
        spotlight.attenuationStartDistance = 5f
        spotlight.attenuationEndDistance = 10f
        spotlight.innerAngle = 5f
        spotlight.outerAngle = 20f
        spotlight.color = Color.GRAY.toLong()
        spotlight.intensity = 800f

        val ambientLight = AmbientLight(Color.WHITE.toLong(), 100F)

//        avatar.setRotation(Vector(0.0, -1.0, 0.0))

        val cameraNode = Node()
        cameraNode.setPosition(Vector(0.0, 0.5, 0.5))
        cameraNode.setRotation(Vector(0.0, 0.0, 0.0))

        val camera = Camera()
        cameraNode.camera = camera

        mViroView.setPointOfView(cameraNode)

        rootNode.addChildNode(avatar)
        rootNode.addChildNode(cameraNode)
        rootNode.addLight(ambientLight)
        rootNode.addLight(spotlight)
        // Display the scene
        mViroView.setScene(scene)
    }

    override fun onStart() {
        super.onStart()
        mViroView.onActivityStarted(this)
    }

    override fun onResume() {
        super.onResume()
        mViroView.onActivityResumed(this)
    }

    override fun onPause() {
        super.onPause()
        mViroView.onActivityPaused(this)
    }

    override fun onStop() {
        super.onStop()
        mViroView.onActivityStopped(this)
    }


}